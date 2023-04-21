package epam.training.kubernetes.post.services;

import epam.training.kubernetes.post.entities.Post;
import epam.training.kubernetes.post.repositories.PostRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class PostServiceTest {

    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private RestTemplate restTemplate;

    private Post post;

    @BeforeEach
    public void setUp() {
        post = new Post(1L, "some text here");
    }

    @Test
    public void testGetAllPosts() {
        List<Post> posts = Collections.singletonList(post);
        Mockito.when(postRepository.findAll()).thenReturn(posts);

        List<Post> result = postService.getAllPosts();

        Assertions.assertEquals(posts, result);
    }

    @Test
    public void testGetPostById() {
        Mockito.when(postRepository.findById(1L)).thenReturn(Optional.of(post));

        Post result = postService.getPostById(1L);

        Assertions.assertEquals(post, result);
    }

    @Test
    public void testGetPostByIdNotFound() {
        Mockito.when(postRepository.findById(1L)).thenReturn(Optional.empty());

        Post result = postService.getPostById(1L);

        Assertions.assertNull(result);
    }

    @Test
    public void testAddPost() {
        Mockito.when(postRepository.save(post)).thenReturn(post);

        Post result = postService.addPost(post);

        Assertions.assertEquals(post, result);
    }

    @Test
    public void testUpdatePost() {
        Mockito.when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        Mockito.when(postRepository.save(post)).thenReturn(post);

        Post result = postService.updatePost(1L, new Post(null, "Updated"));

        Assertions.assertEquals(post, result);
        Assertions.assertEquals(post.getText(), "Updated");
    }

    @Test
    public void testUpdatePostNotFound() {
        Mockito.when(postRepository.findById(1L)).thenReturn(Optional.empty());

        Post result = postService.updatePost(1L, post);

        Assertions.assertNull(result);
    }

    @Test
    public void testDeletePost() {
        Mockito.when(postRepository.findById(1L)).thenReturn(Optional.of(post));
        Mockito.doNothing().when(postRepository).delete(post);

        boolean result = postService.deletePost(1L);

        Assertions.assertTrue(result);
    }

    @Test
    public void testDeletePostNotFound() {
        Mockito.when(postRepository.findById(1L)).thenReturn(Optional.empty());

        boolean result = postService.deletePost(1L);

        Assertions.assertFalse(result);
    }

}