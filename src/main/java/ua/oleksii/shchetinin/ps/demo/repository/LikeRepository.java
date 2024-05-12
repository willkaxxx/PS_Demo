package ua.oleksii.shchetinin.ps.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ua.oleksii.shchetinin.ps.demo.model.Like;

import java.util.List;

public interface LikeRepository extends MongoRepository<Like, String> {
    List<Like> getAllByPostId(String postId);
    void deleteLikeByUsernameAndPostId(String username, String postId);
}
