package epam.training.kubernetes.post.entities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class PostTest {

    @Test
    public void testConstructorAndGetters() {
        Post post = new Post(1L, "some text here");

        Assertions.assertNull(post.getId());
        Assertions.assertNotNull(post.getPostedAt());
        Assertions.assertEquals(post.getAuthorId(), 1L);
        Assertions.assertEquals(post.getText(), "some text here");
    }

}
