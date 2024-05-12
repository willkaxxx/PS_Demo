package ua.oleksii.shchetinin.ps.demo.service;

import ua.oleksii.shchetinin.ps.demo.dto.responce.FeedDto;
import ua.oleksii.shchetinin.ps.demo.model.Post;
import ua.oleksii.shchetinin.ps.demo.model.User;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();

    User getUserByUsername(String username);

    User updateUser(User user);

    User addPostToFavorite(User user, String postId);

    User deletePostFromFavorites(User user, String postId);

    User subscribe(User user, String subscribeTo);

    User unsubscribe(User user, String unsubscribeFrom);

    FeedDto<FeedDto.FullPost> getExtendedFeed(User user);

    FeedDto<Post> getFeed(User user);

    void deleteUser(User user);
}
