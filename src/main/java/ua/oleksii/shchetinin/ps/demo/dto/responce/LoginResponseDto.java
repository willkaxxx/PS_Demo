package ua.oleksii.shchetinin.ps.demo.dto.responce;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponseDto {
    private String token;
    private long expiresIn;
}
