package ua.oleksii.shchetinin.ps.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ua.oleksii.shchetinin.ps.demo.dto.request.AddCommentDto;
import ua.oleksii.shchetinin.ps.demo.dto.request.CreatePostDto;
import ua.oleksii.shchetinin.ps.demo.dto.request.FavoritePostDto;
import ua.oleksii.shchetinin.ps.demo.dto.responce.UserResponseDto;
import ua.oleksii.shchetinin.ps.demo.mapper.UserMapper;
import ua.oleksii.shchetinin.ps.demo.model.Comment;
import ua.oleksii.shchetinin.ps.demo.model.Like;
import ua.oleksii.shchetinin.ps.demo.model.Post;
import ua.oleksii.shchetinin.ps.demo.model.User;
import ua.oleksii.shchetinin.ps.demo.service.CommentService;
import ua.oleksii.shchetinin.ps.demo.service.LikeService;
import ua.oleksii.shchetinin.ps.demo.service.PostService;
import ua.oleksii.shchetinin.ps.demo.service.UserService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final UserService userService;
    private final UserMapper userMapper;
    private final CommentService commentService;
    private final LikeService likeService;

    @GetMapping
    public ResponseEntity<List<Post>> getPosts(@RequestParam(value = "authorName") String authorName) {
        return ResponseEntity.ok(postService.getPostByAuthorUsername(authorName));
    }

    @PostMapping
    public ResponseEntity<List<Post>> addPost(@AuthenticationPrincipal User author, @RequestBody CreatePostDto createPostDto) {
        var savedPost = postService.addPost(Post.builder()
                .authorUsername(author.getUsername())
                .postText(createPostDto.getPostText())
                .build());
        return ResponseEntity.ok(List.of(savedPost));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@AuthenticationPrincipal User user, @PathVariable(value = "id") String postId, @RequestBody CreatePostDto createPostDto) {
        var updatedPost = postService.updatePost(Post.builder()
                        .id(postId)
                        .postText(createPostDto.getPostText())
                        .build(),
                user);
        return ResponseEntity.ok(updatedPost);
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<Like> likePost(@AuthenticationPrincipal User user, @PathVariable(value = "id") String postId) {
        var like = likeService.like(user.getUsername(), postId);
        return ResponseEntity.ok(like);
    }

    @DeleteMapping("/{id}/like")
    public ResponseEntity<String> unlikePost(@AuthenticationPrincipal User user, @PathVariable(value = "id") String postId) {
        likeService.removeLike(user.getUsername(), postId);
        return ResponseEntity.ok("Like removed");
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<Comment> addComment(@AuthenticationPrincipal User user, @PathVariable(value = "id") String postId, @RequestBody AddCommentDto addCommentDto) {
        var comment = commentService.addComment(postId, Comment.builder()
                .authorUsername(user.getUsername())
                .postId(postId)
                .text(addCommentDto.getText())
                .build());
        return ResponseEntity.ok(comment);
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<Collection<Comment>> getComments(@PathVariable(value = "id") String postId) {
        var comments = commentService.getPostComments(postId);
        return ResponseEntity.ok(comments);
    }

    @PostMapping("/favorites")
    public ResponseEntity<UserResponseDto> addPostToFavorite(@AuthenticationPrincipal User user, @RequestBody FavoritePostDto favoritePostDto) {
        var updatedUser = userService.addPostToFavorite(user, favoritePostDto.getPostId());
        return ResponseEntity.ok(userMapper.userToDto(updatedUser));
    }

    @DeleteMapping("/favorites")
    public ResponseEntity<UserResponseDto> deletePostToFavorite(@AuthenticationPrincipal User user, @RequestBody FavoritePostDto favoritePostDto) {
        var updatedUser = userService.deletePostFromFavorites(user, favoritePostDto.getPostId());
        return ResponseEntity.ok(userMapper.userToDto(updatedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@AuthenticationPrincipal User user, @PathVariable(value = "id") String postId) {
        postService.deletePost(postId, user);
        return ResponseEntity.ok("Post with id:%s was deleted".formatted(postId));
    }

}
