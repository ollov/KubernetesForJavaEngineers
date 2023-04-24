package epam.training.kubernetes.user.exceptions;

public class LimitReachedException extends RuntimeException {
    public LimitReachedException(final String suffix) {
        super("The user has reached the post limit's " + suffix);
    }
}
