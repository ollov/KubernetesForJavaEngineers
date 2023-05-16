package epam.training.kubernetes.user.controllers;

import epam.training.kubernetes.user.entities.User;
import epam.training.kubernetes.user.exceptions.LimitReachedException;
import epam.training.kubernetes.user.services.IUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final static Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private final IUserService userService;

    @Autowired
    public UserController(final IUserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<User>> findAll() {
        return ResponseEntity.ok(this.userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> find(final @PathVariable(value = "id") Long id) {
        LOGGER.info("Find user for id {}", id);
        final User user = this.userService.getUserById(id);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping
    public ResponseEntity<User> create(final @RequestBody User user) {
        LOGGER.info("Create new user {}", user.getUsername());
        User newUser = userService.addUser(user);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newUser.getId())
                .toUri();
        return ResponseEntity.created(location).body(newUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(final @PathVariable(value = "id") Long id, final @RequestBody User user) {
        LOGGER.info("Update user with id {}, set name {}", id, user.getUsername());
        final User updatedUser = this.userService.updateUser(id, user);
        if (updatedUser == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(final @PathVariable(value = "id") Long id) {
        LOGGER.info("Delete user with id {}", id);
        if(!this.userService.deleteUser(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/increment")
    public ResponseEntity<Void> incrementPosts(final @PathVariable(value = "id") Long id) {
        LOGGER.info("Increment post for user id {}", id);
        if(!this.userService.incrementPosts(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/decrement")
    public ResponseEntity<Void> decrementPosts(final @PathVariable(value = "id") Long id) {
        LOGGER.info("Decrement post for user id {}", id);
        if(!this.userService.decrementPosts(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(LimitReachedException.class)
    public String handleValidationExceptions(LimitReachedException ex) {
        return ex.getMessage();
    }

}
