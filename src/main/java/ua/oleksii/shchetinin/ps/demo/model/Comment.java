package ua.oleksii.shchetinin.ps.demo.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

@Data
@Builder
public class Comment {
    @Id
    private String id;
    @Indexed
    private String authorUsername;
    @Indexed
    private String postId;
    private String text;
}
