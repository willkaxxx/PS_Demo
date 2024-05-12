package ua.oleksii.shchetinin.ps.demo.service.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ua.oleksii.shchetinin.ps.demo.dto.request.RegisterUserDto;
import ua.oleksii.shchetinin.ps.demo.model.User;
import ua.oleksii.shchetinin.ps.demo.repository.UserRepository;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public User signup(RegisterUserDto input) {
        User user = User.builder()
                .username(input.getUsername())
                .password(passwordEncoder.encode(input.getPassword()))
                .build();

        return userRepository.save(user);
    }

    public User authenticate(String credentials) {
        if (credentials.startsWith("Basic")) {
            byte[] credDecoded = Base64.getDecoder().decode(credentials.substring("Basic".length()).trim());
            String decodedCredentials = new String(credDecoded, StandardCharsets.UTF_8);
            final String[] values = decodedCredentials.split(":", 2);
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            values[0],
                            values[1]
                    )
            );

            return userRepository.findByUsername(values[0])
                    .orElseThrow();
        } else {
            throw new AccessDeniedException("Wrong credentials");
        }
    }
}
