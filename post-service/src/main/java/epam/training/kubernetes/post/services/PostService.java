package epam.training.kubernetes.post.services;

import epam.training.kubernetes.post.entities.Post;
import epam.training.kubernetes.post.entities.User;
import epam.training.kubernetes.post.exceptions.AuthorNotFoundException;
import epam.training.kubernetes.post.repositories.PostRepository;
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
        try {
            restTemplate.getForObject(usersServiceHost + "/users/{id}", User.class, authorId);
        }
        catch (RestClientException e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new AuthorNotFoundException(authorId);
        }
        try {

            restTemplate.postForLocation(usersServiceHost + "/users/{id}/increment", Void.class, authorId);
            return this.postRepository.save(post);
        }
        catch (RestClientException e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw e;
        }
    }

    @Override
    public Post updatePost(final Long id, final Post post) {
        final Post existingPost = postRepository.findById(id).orElse(null);
        if (existingPost != null) {
            existingPost.setText(post.getText());
            return postRepository.save(existingPost);
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public boolean deletePost(final Long id) {
        final Optional<Post> existingPost = postRepository.findById(id);
        if (existingPost.isPresent()) {
            try {
                restTemplate.postForLocation(usersServiceHost + "/users/{id}/decrement", Void.class,
                        existingPost.get().getAuthorId());
                postRepository.deleteById(id);
                return true;
            }
            catch (RestClientException e) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                throw e;
            }
        } else {
            return false;
        }
    }
}
