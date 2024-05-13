package ua.oleksii.shchetinin.ps.demo.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.oleksii.shchetinin.ps.demo.dto.request.RegisterUserDto;
import ua.oleksii.shchetinin.ps.demo.dto.responce.LoginResponseDto;
import ua.oleksii.shchetinin.ps.demo.dto.responce.UserResponseDto;
import ua.oleksii.shchetinin.ps.demo.mapper.UserMapper;
import ua.oleksii.shchetinin.ps.demo.model.User;
import ua.oleksii.shchetinin.ps.demo.service.security.AuthenticationService;
import ua.oleksii.shchetinin.ps.demo.service.security.JwtService;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
@Log4j2
public class AuthController {

    private final JwtService jwtService;
    private final AuthenticationService authenticationService;
    private final UserMapper userMapper;

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDto> register(@RequestBody RegisterUserDto registerUserDto) {
        User registeredUser = authenticationService.signup(registerUserDto);

        UserResponseDto userResponseDto = userMapper.userToDto(registeredUser);
        log.info("User {} signed up", userResponseDto);
        return ResponseEntity.ok(userResponseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> authenticate(@RequestHeader("authorization") String credentials) {
        User authenticatedUser = authenticationService.authenticate(credentials);

        String jwtToken = jwtService.generateToken(org.springframework.security.core.userdetails.User.builder()
                .username(authenticatedUser.getUsername())
                .password(authenticatedUser.getPassword())
                .build());

        LoginResponseDto loginResponse = LoginResponseDto.builder()
                .token(jwtToken)
                .expiresIn(jwtService.getExpirationTime())
                .build();
        log.info("User {} logged in", authenticatedUser.getUsername());
        return ResponseEntity.ok(loginResponse);
    }
}
