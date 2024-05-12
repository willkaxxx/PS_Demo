package ua.oleksii.shchetinin.ps.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import ua.oleksii.shchetinin.ps.demo.model.User;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
}
