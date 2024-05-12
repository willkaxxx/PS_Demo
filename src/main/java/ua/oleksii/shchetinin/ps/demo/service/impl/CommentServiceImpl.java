package ua.oleksii.shchetinin.ps.demo.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.oleksii.shchetinin.ps.demo.exception.PostNotFoundException;
import ua.oleksii.shchetinin.ps.demo.model.Comment;
import ua.oleksii.shchetinin.ps.demo.repository.CommentRepository;
import ua.oleksii.shchetinin.ps.demo.repository.PostRepository;
import ua.oleksii.shchetinin.ps.demo.service.CommentService;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Override public Comment addComment(String postId, Comment comment) {
        var postIsPresent = postRepository.existsById(postId);

        if (postIsPresent) {
            return commentRepository.save(comment);
        } else {
            throw new PostNotFoundException("Post with id %s is not found".formatted(postId));
        }
    }

    @Override public Collection<Comment> getPostComments(String postId) {
        return commentRepository.findAllByPostId(postId);
    }
}
