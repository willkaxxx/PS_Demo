package ua.oleksii.shchetinin.ps.demo.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterUserDto {
    private String username;
    private String password;
    private String email;
}
