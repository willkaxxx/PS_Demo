package ua.oleksii.shchetinin.ps.demo.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import ua.oleksii.shchetinin.ps.demo.model.Post;
import ua.oleksii.shchetinin.ps.demo.model.User;
import ua.oleksii.shchetinin.ps.demo.repository.CommentRepository;
import ua.oleksii.shchetinin.ps.demo.repository.PostRepository;
import ua.oleksii.shchetinin.ps.demo.service.PostService;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Override public List<Post> getPostByAuthorUsername(String username) {
        return postRepository.findByAuthorUsername(username);
    }

    @Override public Post addPost(Post post) {
        return postRepository.save(post);
    }

    @Override public Post updatePost(Post post, User initiator) {
        Optional<Post> postToUpdate = postRepository.findById(post.getId());
        if (postToUpdate.isPresent() && initiatorAuthorizedToManagePost(initiator, postToUpdate.get())) {
            var postToSave = postToUpdate.get().toBuilder().postText(post.getPostText()).build();
            return postRepository.save(postToSave);
        } else {
            throw new AccessDeniedException("You do not have access to this post");
        }
    }

    @Override public void deletePost(String postId, User initiator) {
        Optional<Post> postToDelete = postRepository.findById(postId);
        postToDelete.ifPresent(post -> {
            if (initiatorAuthorizedToManagePost(initiator, post)) {
                postRepository.deleteById(postId);
                commentRepository.deleteAllByPostId(postId);
            } else {
                throw new AccessDeniedException("You do not have access to this post");
            }
        });
    }

    private static boolean initiatorAuthorizedToManagePost(User initiator, Post post) {
        return post.getAuthorUsername().equals(initiator.getUsername());
    }

}
