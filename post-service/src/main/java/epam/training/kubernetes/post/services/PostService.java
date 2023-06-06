package epam.training.kubernetes.post.services;

import epam.training.kubernetes.post.entities.Post;
import epam.training.kubernetes.post.entities.User;
import epam.training.kubernetes.post.exceptions.AuthorNotFoundException;
import epam.training.kubernetes.post.repositories.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class PostService implements IPostService {

    private final static Logger LOGGER = LoggerFactory.getLogger(PostService.class);

    private final PostRepository postRepository;

    private final RestTemplate restTemplate;

    @Value("${users.service.host}")
    private String usersServiceHost;

    @Autowired
    public PostService(final PostRepository postRepository, final RestTemplate restTemplate) {
        super();
        this.postRepository = postRepository;
        this.restTemplate = restTemplate;
    }

    @Override
    public List<Post> getAllPosts() {
        return this.postRepository.findAll();
    }

    @Override
    public Post getPostById(final Long id) {
        return this.postRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Post addPost(final Post post) {
        final Long authorId = post.getAuthorId();
        LOGGER.info("Adding post for author {}", authorId);
        try {
            restTemplate.getForObject(usersServiceHost + "/users/{id}", User.class, authorId);
        }
        catch (RestClientException e) {
            LOGGER.info("author {} not found", authorId);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new AuthorNotFoundException(authorId);
        }
        try {
            restTemplate.postForLocation(usersServiceHost + "/users/{id}/increment", Void.class, authorId);
            return this.postRepository.save(post);
        }
        catch (RestClientException e) {
            LOGGER.info("Failed to increment posts for author {}", authorId);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw e;
        }
    }

    @Override
    public Post updatePost(final Long id, final Post post) {
        LOGGER.info("Updating post with id {}", id);
        final Post existingPost = postRepository.findById(id).orElse(null);
        if (existingPost != null) {
            existingPost.setText(post.getText());
            existingPost.setTopic(post.getTopic());
            return postRepository.save(existingPost);
        } else {
            LOGGER.info("Failed to find post for id {}", id);
            return null;
        }
    }

    @Override
    @Transactional
    public boolean deletePost(final Long id) {
        LOGGER.info("Deleting post with id {}", id);
        final Optional<Post> existingPost = postRepository.findById(id);
        if (existingPost.isPresent()) {
            try {
                restTemplate.postForLocation(usersServiceHost + "/users/{id}/decrement", Void.class,
                        existingPost.get().getAuthorId());
                postRepository.deleteById(id);
                return true;
            }
            catch (RestClientException e) {
                LOGGER.info("Failed to decrement posts for author {}", existingPost.get().getAuthorId());
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                throw e;
            }
        } else {
            LOGGER.info("Failed to find post for id {}", id);
            return false;
        }
    }
}
