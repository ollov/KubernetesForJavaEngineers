package epam.training.kubernetes.user.services;

import epam.training.kubernetes.user.entities.User;
import epam.training.kubernetes.user.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User("John");
    }

    @Test
    public void testGetAllUsers() {
        List<User> users = Collections.singletonList(user);
        Mockito.when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUsers();

        Assertions.assertEquals(users, result);
    }

    @Test
    public void testGetUserById() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userService.getUserById(1L);

        Assertions.assertEquals(user, result);
    }

    @Test
    public void testGetUserByIdNotFound() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());

        User result = userService.getUserById(1L);

        Assertions.assertNull(result);
    }

    @Test
    public void testAddUser() {
        Mockito.when(userRepository.save(user)).thenReturn(user);

        User result = userService.addUser(user);

        Assertions.assertEquals(user, result);
    }

    @Test
    public void testUpdateUser() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(user)).thenReturn(user);

        User result = userService.updateUser(1L, new User("Updated"));

        Assertions.assertEquals(user, result);
        Assertions.assertEquals(user.getUsername(), "Updated");
    }

    @Test
    public void testUpdateUserNotFound() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());

        User result = userService.updateUser(1L, user);

        Assertions.assertNull(result);
    }

    @Test
    public void testDeleteUser() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.doNothing().when(userRepository).delete(user);

        boolean result = userService.deleteUser(1L);

        Assertions.assertTrue(result);
    }

    @Test
    public void testDeleteUserNotFound() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());

        boolean result = userService.deleteUser(1L);

        Assertions.assertFalse(result);
    }

    @Test
    public void testIncrementPosts() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(user)).thenReturn(user);

        boolean result = userService.incrementPosts(1L);

        Assertions.assertTrue(result);
        Assertions.assertEquals(user.getAmountOfPosts(), 1L);
    }

    @Test
    public void testIncrementPostsUserNotFound() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());

        boolean result = userService.incrementPosts(1L);

        Assertions.assertFalse(result);
    }

    @Test
    public void testDecrementPosts() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(user)).thenReturn(user);
        user.setAmountOfPosts(1L);

        boolean result = userService.decrementPosts(1L);

        Assertions.assertTrue(result);
        Assertions.assertEquals(user.getAmountOfPosts(), 0L);
    }

    @Test
    public void testDecrementPostsUserNotFound() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());

        boolean result = userService.decrementPosts(1L);

        Assertions.assertFalse(result);
    }

}