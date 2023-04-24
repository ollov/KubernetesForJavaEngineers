package epam.training.kubernetes.post.entities;

public class User {

    private Long id;

    private String username;

    private Long amountOfPosts;

    public User() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getAmountOfPosts() {
        return amountOfPosts;
    }

    public void setAmountOfPosts(Long amountOfPosts) {
        this.amountOfPosts = amountOfPosts;
    }
}
