package ua.oleksii.shchetinin.ps.demo.service;

import ua.oleksii.shchetinin.ps.demo.model.Like;

public interface LikeService {
    Like like(String username, String postId);

    void removeLike(String username, String postId);
}
