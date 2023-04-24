package epam.training.kubernetes.post.exceptions;

public class AuthorNotFoundException extends RuntimeException {
    public AuthorNotFoundException(final Long authorId) {
        super("Author not found, id: " + authorId);
    }
}
