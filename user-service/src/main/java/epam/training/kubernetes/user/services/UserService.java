package epam.training.kubernetes.user.services;

import epam.training.kubernetes.user.exceptions.LimitReachedException;
import epam.training.kubernetes.user.repositories.UserRepository;
import epam.training.kubernetes.user.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements IUserService {

    private final static Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    @Autowired
    public UserService(final UserRepository userRepository) {
        super();
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    @Override
    public User getUserById(final Long id) {
        return this.userRepository.findById(id).orElse(null);
    }

    @Override
    public User addUser(final User user) {
        return this.userRepository.save(user);
    }

    @Override
    public User updateUser(final Long id, final User user) {
        LOGGER.info("Updating user for id {}", id);
        final User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser != null) {
            existingUser.setUsername(user.getUsername());
            return userRepository.save(existingUser);
        } else {
            LOGGER.info("Failed to find user for id {}", id);
            return null;
        }
    }

    @Override
    public boolean deleteUser(final Long id) {
        LOGGER.info("Deleting user for id {}", id);
        final Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            userRepository.deleteById(id);
            return true;
        } else {
            LOGGER.info("Failed to find user for id {}", id);
            return false;
        }
        //TODO Either do not remove user if there are posts or remove all posts for consistency
    }

    @Override
    public boolean incrementPosts(final Long id) {
        return updateAmountOfPosts(id, 1);
    }

    @Override
    public boolean decrementPosts(final Long id) {
        return updateAmountOfPosts(id, -1);
    }

    private boolean updateAmountOfPosts(final Long id, final int delta) {
        LOGGER.info("Updating user for id {}", id);
        final User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser != null) {
            final long amountOfPosts = existingUser.getAmountOfPosts() + delta;
            if(amountOfPosts >= 0 && amountOfPosts <= Integer.MAX_VALUE) {
                LOGGER.info("Setting amount {} for user for id {}", amountOfPosts, id);
                existingUser.setAmountOfPosts(amountOfPosts);
                userRepository.save(existingUser);
                return true;
            }
            else {
                throw new LimitReachedException(delta == 1 ? "maximun" : "minimum");
            }
        }
        LOGGER.info("Failed to find user for id {}", id);
        return false;
    }

}
