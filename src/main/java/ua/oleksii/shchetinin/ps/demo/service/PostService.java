package ua.oleksii.shchetinin.ps.demo.service;

import ua.oleksii.shchetinin.ps.demo.model.Post;
import ua.oleksii.shchetinin.ps.demo.model.User;

import java.util.List;

public interface PostService {
    List<Post> getPostByAuthorUsername(String username);

    Post addPost(Post post);

    Post updatePost(Post post, User initiator);

    void deletePost(String postId, User initiator);
}
