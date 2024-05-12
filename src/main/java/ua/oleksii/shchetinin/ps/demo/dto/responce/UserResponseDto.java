package ua.oleksii.shchetinin.ps.demo.dto.responce;

import lombok.Data;

import java.util.Set;

@Data
public class UserResponseDto {

    private String id;
    private String username;
    private String email;
    private Set<String> favoritePosts;
    private Set<String> subscriptions;

}
