package ua.oleksii.shchetinin.ps.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ua.oleksii.shchetinin.ps.demo.model.Post;

import java.util.List;

public interface PostRepository extends MongoRepository<Post, String> {
    List<Post> findByAuthorUsername(String username);
}
