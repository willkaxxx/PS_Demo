package ua.oleksii.shchetinin.ps.demo.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ua.oleksii.shchetinin.ps.demo.Utils;
import ua.oleksii.shchetinin.ps.demo.dto.responce.FeedDto;
import ua.oleksii.shchetinin.ps.demo.dto.responce.FeedDto.FullPost;
import ua.oleksii.shchetinin.ps.demo.exception.PostNotFoundException;
import ua.oleksii.shchetinin.ps.demo.exception.SelfSubscribeException;
import ua.oleksii.shchetinin.ps.demo.model.Like;
import ua.oleksii.shchetinin.ps.demo.model.Post;
import ua.oleksii.shchetinin.ps.demo.model.User;
import ua.oleksii.shchetinin.ps.demo.repository.CommentRepository;
import ua.oleksii.shchetinin.ps.demo.repository.LikeRepository;
import ua.oleksii.shchetinin.ps.demo.repository.PostRepository;
import ua.oleksii.shchetinin.ps.demo.repository.UserRepository;
import ua.oleksii.shchetinin.ps.demo.service.UserService;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public User updateUser(User user) {
        User savedUser = getUserByUsername(user.getUsername());
        User save = userRepository.save(savedUser.toBuilder()
                .email(user.getEmail())
                .build());
        log.info("User {} saved", user);
        return save;
    }

    @Override
    public User addPostToFavorite(User user, String postId) {
        var postExists = postRepository.existsById(postId);
        if (!postExists) {
            throw new PostNotFoundException("Post with id %s not found".formatted(postId));
        }
        User savedUser = getUserByUsername(user.getUsername());
        if (Objects.isNull(savedUser.getFavoritePosts())) {
            savedUser.setFavoritePosts(Set.of(postId));
        } else {
            savedUser.getFavoritePosts().add(postId);
        }
        return userRepository.save(savedUser);
    }

    @Override
    public User deletePostFromFavorites(User user, String postId) {
        User savedUser = getUserByUsername(user.getUsername());
        if (Objects.nonNull(savedUser.getFavoritePosts())) {
            savedUser.getFavoritePosts().remove(postId);
        }
        return userRepository.save(savedUser);
    }

    @Override
    public User subscribe(User user, String subscribeTo) {
        User savedUser = getUserByUsername(user.getUsername());
        if (subscribeTo.equals(user.getUsername())) {
            throw new SelfSubscribeException("You can't subscribe to yourself");
        }
        if (!userRepository.existsByUsername(subscribeTo)) {
            throw new UsernameNotFoundException("User not found");
        }

        if (Objects.isNull(savedUser.getSubscriptions())) {
            savedUser.setSubscriptions(Set.of(subscribeTo));
        } else {
            savedUser.getSubscriptions().add(subscribeTo);
        }
        return userRepository.save(savedUser);
    }

    @Override
    public User unsubscribe(User user, String unsubscribeFrom) {
        User savedUser = getUserByUsername(user.getUsername());
        if (Objects.nonNull(savedUser.getFavoritePosts())) {
            savedUser.getSubscriptions().remove(unsubscribeFrom);
        }
        return userRepository.save(savedUser);
    }

    @Override
    public FeedDto<FullPost> getExtendedFeed(User user) {
        var savedUser = getUserByUsername(user.getUsername());
        return FeedDto.<FullPost>builder()
                .feed(Utils.safeStream(savedUser.getSubscriptions())
                        .flatMap(this::getUserPosts)
                        .map(post -> FullPost.builder().post(post).build())
                        .map(this::enrichWithLikes)
                        .map(this::enrichWithComments)
                        .toList())
                .build();
    }

    @Override
    public FeedDto<Post> getFeed(User user) {
        var savedUser = getUserByUsername(user.getUsername());
        return FeedDto.<Post>builder()
                .feed(Utils.safeStream(savedUser.getSubscriptions())
                        .flatMap(this::getUserPosts)
                        .toList())
                .build();
    }

    private Stream<Post> getUserPosts(String userId) {
        return userRepository.findByUsername(userId)
                .map(User::getUsername)
                .map(postRepository::findByAuthorUsername)
                .orElse(List.of()).stream();
    }

    private FullPost enrichWithLikes(FullPost fullPost) {
        var postId = fullPost.getPost().getId();
        fullPost.setLikedBy(likeRepository.getAllByPostId(postId).stream()
                .map(Like::getUsername)
                .distinct()
                .toList());
        return fullPost;
    }

    private FullPost enrichWithComments(FullPost fullPost) {
        var postId = fullPost.getPost().getId();
        fullPost.setComments(commentRepository.findAllByPostId(postId));
        return fullPost;
    }

    @Override
    public void deleteUser(User user) {
        userRepository.delete(getUserByUsername(user.getUsername()));
    }
}
