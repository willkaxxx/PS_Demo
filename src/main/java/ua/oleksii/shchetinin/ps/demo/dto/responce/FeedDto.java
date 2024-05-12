package ua.oleksii.shchetinin.ps.demo.dto.responce;

import lombok.Builder;
import lombok.Data;
import ua.oleksii.shchetinin.ps.demo.model.Comment;
import ua.oleksii.shchetinin.ps.demo.model.Post;

import java.util.List;

@Data
@Builder
public class FeedDto<T> {
    private List<T> feed;

    @Data
    @Builder
    public static final class FullPost {
        private Post post;
        private List<String> likedBy;
        private List<Comment> comments;
    }
}
