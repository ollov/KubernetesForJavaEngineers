package epam.training.kubernetes.user.controllers;

import epam.training.kubernetes.user.entities.User;
import epam.training.kubernetes.user.services.UserService;
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
public class UserControllerTest {

    private final static Long USER_ID = 1L;

    private final static String USER_NAME = "John";

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private UserService userService;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User("John");
        user.setId(USER_ID);
    }

    @Test
    public void getAllUsers() {
        Mockito.when(userService.getAllUsers()).thenReturn(Arrays.asList(user));

        ResponseEntity<List<User>> response = restTemplate.exchange("/users", HttpMethod.GET,null,
                new ParameterizedTypeReference<List<User>>() {});

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(USER_NAME, response.getBody().get(0).getUsername());
    }

    @Test
    public void testGetUserById() {
        Mockito.when(userService.getUserById(USER_ID)).thenReturn(user);

        ResponseEntity<User> response = restTemplate.getForEntity("/users/{id}", User.class, USER_ID);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(USER_ID, response.getBody().getId());
        Assertions.assertEquals(USER_NAME, response.getBody().getUsername());
    }

    @Test
    public void testAddUser() {
        Mockito.when(userService.addUser(user)).thenReturn(user);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<User> request = new HttpEntity<>(user, headers);

        ResponseEntity<User> response = restTemplate.postForEntity("/users", request, User.class);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertEquals(user, response.getBody());
    }

    @Test
    public void testUpdateUser() {
        Mockito.when(userService.updateUser(USER_ID, user)).thenReturn(user);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<User> request = new HttpEntity<>(user, headers);

        ResponseEntity<User> response = restTemplate.exchange("/users/{id}", HttpMethod.PUT, request, User.class, USER_ID);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(user, response.getBody());
    }

    @Test
    public void testDeleteUser() {
        Mockito.when(userService.deleteUser(USER_ID)).thenReturn(true);

        restTemplate.delete("/users/" + USER_ID);

        Mockito.verify(userService).deleteUser(USER_ID);
    }

}