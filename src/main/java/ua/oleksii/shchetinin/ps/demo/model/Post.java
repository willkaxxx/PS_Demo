package ua.oleksii.shchetinin.ps.demo.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

@Data
@Builder(toBuilder = true)
public class Post {
    @Id
    private String id;
    @Indexed
    private String authorUsername;
    private String postText;
}
