package epam.training.kubernetes.user.services;

import epam.training.kubernetes.user.entities.User;

import java.util.List;

public interface IUserService {

    List<User> getAllUsers();

    User getUserById(final Long id);

    User addUser(final User user);

    User updateUser(final Long id, final User user);

    boolean deleteUser(final Long id);

    boolean incrementPosts(final Long id);

    boolean decrementPosts(final Long id);
}
