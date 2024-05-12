package ua.oleksii.shchetinin.ps.demo.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ua.oleksii.shchetinin.ps.demo.exception.PostNotFoundException;
import ua.oleksii.shchetinin.ps.demo.model.Like;
import ua.oleksii.shchetinin.ps.demo.repository.LikeRepository;
import ua.oleksii.shchetinin.ps.demo.repository.PostRepository;
import ua.oleksii.shchetinin.ps.demo.service.LikeService;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {

    private final PostRepository postRepository;
    private final LikeRepository likeRepository;

    @Override public Like like(String username, String postId) {
        var post = postRepository.findById(postId);

        if (post.isPresent()) {
            return likeRepository.save(Like.builder()
                    .username(username)
                    .postId(postId)
                    .build()
            );
        } else {
            throw new PostNotFoundException("Post with id %s is not found".formatted(postId));
        }
    }

    @Override public void removeLike(String username, String postId) {
        var post = postRepository.findById(postId);

        if (post.isPresent()) {
            likeRepository.deleteLikeByUsernameAndPostId(username, postId);
        } else {
            throw new PostNotFoundException("Post with id %s is not found".formatted(postId));
        }
    }
}
