package ua.oleksii.shchetinin.ps.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ua.oleksii.shchetinin.ps.demo.model.Comment;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, String> {
    List<Comment> findAllByPostId(String postId);
    void deleteAllByPostId(String postId);
}
