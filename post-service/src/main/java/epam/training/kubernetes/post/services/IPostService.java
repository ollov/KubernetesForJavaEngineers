package epam.training.kubernetes.post.services;

import epam.training.kubernetes.post.entities.Post;

import java.util.List;

public interface IPostService {

    List<Post> getAllPosts();

    Post getPostById(final Long id);

    Post addPost(final Post post);

    Post updatePost(final Long id, final Post post);

    boolean deletePost(final Long id);
}
