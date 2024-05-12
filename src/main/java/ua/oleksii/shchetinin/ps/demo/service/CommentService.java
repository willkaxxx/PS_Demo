package ua.oleksii.shchetinin.ps.demo.service;

import ua.oleksii.shchetinin.ps.demo.model.Comment;

import java.util.Collection;

public interface CommentService {
    Comment addComment(String postId, Comment comment);

    Collection<Comment> getPostComments(String postId);
}
