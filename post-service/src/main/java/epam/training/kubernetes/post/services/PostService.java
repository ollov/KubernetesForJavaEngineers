package epam.training.kubernetes.post.services;

import epam.training.kubernetes.post.entities.Post;
import epam.training.kubernetes.post.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.http.HttpClient;
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
    public Post addPost(final Post post) {
        Post createdPost = this.postRepository.save(post);
        incrementPostCount(createdPost.getAuthorId());
        return createdPost;
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
    public boolean deletePost(final Long id) {
        final Optional<Post> existingPost = postRepository.findById(id);
        if (existingPost.isPresent()) {
            postRepository.deleteById(id);
            decrementPostCount(existingPost.get().getAuthorId());
            return true;
        } else {
            return false;
        }
    }

    private void incrementPostCount(final Long id) {
        String location = usersServiceHost + "/users/" + id + "/increment";
        restTemplate.postForLocation(location, Void.class);
    }

    private void decrementPostCount(final Long id) {
        String location = usersServiceHost + "/users/" + id + "/decrement";
        restTemplate.postForLocation(location, Void.class);
    }
}
