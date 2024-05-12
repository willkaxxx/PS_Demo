package ua.oleksii.shchetinin.ps.demo.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

@Data
@Builder
public class Like {
    @Id
    private String id;
    @Indexed
    private String username;
    @Indexed
    private String postId;
}
