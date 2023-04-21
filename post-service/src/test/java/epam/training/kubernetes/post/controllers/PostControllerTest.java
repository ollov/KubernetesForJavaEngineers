package epam.training.kubernetes.post.controllers;

import epam.training.kubernetes.post.entities.Post;
import epam.training.kubernetes.post.services.PostService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PostControllerTest {

    private final static Long POST_ID = 5L;

    private final static String TEXT = "some text here";

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private PostService postService;

    private Post post;

    @BeforeEach
    public void setUp() {
        post = new Post(1L, TEXT);
        post.setId(POST_ID);
    }

    @Test
    public void getAllPosts() {
        Mockito.when(postService.getAllPosts()).thenReturn(Arrays.asList(post));

        ResponseEntity<List<Post>> response = restTemplate.exchange("/posts", HttpMethod.GET,null,
                new ParameterizedTypeReference<List<Post>>() {});

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(TEXT, response.getBody().get(0).getText());
    }

    @Test
    public void testGetPostById() {
        Mockito.when(postService.getPostById(POST_ID)).thenReturn(post);

        ResponseEntity<Post> response = restTemplate.getForEntity("/posts/{id}", Post.class, POST_ID);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(POST_ID, response.getBody().getId());
        Assertions.assertEquals(TEXT, response.getBody().getText());
    }

    @Test
    public void testAddPost() {
        Mockito.when(postService.addPost(post)).thenReturn(post);
        post.setPostedAt(null);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Post> request = new HttpEntity<>(post, headers);

        ResponseEntity<Post> response = restTemplate.postForEntity("/posts", request, Post.class);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertEquals(post, response.getBody());
    }

    @Test
    public void testUpdatePost() {
        Mockito.when(postService.updatePost(POST_ID, post)).thenReturn(post);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Post> request = new HttpEntity<>(post, headers);

        ResponseEntity<Post> response = restTemplate.exchange("/posts/{id}", HttpMethod.PUT, request, Post.class, POST_ID);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(post, response.getBody());
    }

    @Test
    public void testDeletePost() {
        Mockito.when(postService.deletePost(POST_ID)).thenReturn(true);

        restTemplate.delete("/posts/" + POST_ID);

        Mockito.verify(postService).deletePost(POST_ID);
    }

}