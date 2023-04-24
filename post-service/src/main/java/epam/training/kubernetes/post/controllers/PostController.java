package epam.training.kubernetes.post.controllers;

import epam.training.kubernetes.post.entities.Post;
import epam.training.kubernetes.post.exceptions.AuthorNotFoundException;
import epam.training.kubernetes.post.services.IPostService;
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
@RequestMapping(path = "/posts")
public class PostController {

    private final IPostService postService;

    @Autowired
    public PostController(final IPostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public ResponseEntity<List<Post>> findAll() {
        return ResponseEntity.ok(this.postService.getAllPosts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> find(final @PathVariable(value = "id") Long id) {
        final Post post = this.postService.getPostById(id);
        if (post == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(post);
    }

    @PostMapping
    public ResponseEntity<Post> create(final @RequestBody Post post) {
        Post newPost = this.postService.addPost(post);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newPost.getId())
                .toUri();
        return ResponseEntity.created(location).body(newPost);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> update(final @PathVariable(value = "id") Long id, final @RequestBody Post post) {
        final Post updatedPost = this.postService.updatePost(id, post);
        if (updatedPost == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedPost);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(final @PathVariable(value = "id") Long id) {
        if(!this.postService.deletePost(id)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(AuthorNotFoundException.class)
    public String handleValidationExceptions(AuthorNotFoundException ex) {
        return ex.getMessage();
    }

}
