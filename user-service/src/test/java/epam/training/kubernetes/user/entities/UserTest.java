package epam.training.kubernetes.user.entities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class UserTest {

    @Test
    public void testConstructorAndGetters() {
        User user = new User("username");

        Assertions.assertNull(user.getId());
        Assertions.assertEquals(user.getAmountOfPosts(), 0L);
        Assertions.assertEquals(user.getUsername(), "username");
    }

}
