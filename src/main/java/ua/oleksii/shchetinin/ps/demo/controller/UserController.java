package ua.oleksii.shchetinin.ps.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ua.oleksii.shchetinin.ps.demo.dto.request.UserUpdateDto;
import ua.oleksii.shchetinin.ps.demo.dto.responce.UserResponseDto;
import ua.oleksii.shchetinin.ps.demo.mapper.UserMapper;
import ua.oleksii.shchetinin.ps.demo.model.User;
import ua.oleksii.shchetinin.ps.demo.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAll() {
        var allUsers = userService.getAllUsers();
        return ResponseEntity.ok(userMapper.userToDto(allUsers));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> me(@AuthenticationPrincipal User user) {
        return getUser(user);
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserResponseDto> getUser(@AuthenticationPrincipal User user) {
        User myUser = userService.getUserByUsername(user.getUsername());
        return ResponseEntity.ok(userMapper.userToDto(myUser));
    }

    @PutMapping("/{username}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable String username, @RequestBody UserUpdateDto updateDto) {
        User updatedUser = userService.updateUser(User.builder()
                .username(username)
                .email(updateDto.getEmail())
                .build());
        return ResponseEntity.ok(userMapper.userToDto(updatedUser));
    }

    @PostMapping("/{username}/subscribe")
    public ResponseEntity<UserResponseDto> subscribe(@PathVariable String username, @AuthenticationPrincipal User user) {
        User updatedUser = userService.subscribe(user, username);
        return ResponseEntity.ok(userMapper.userToDto(updatedUser));
    }

    @DeleteMapping("/{username}/subscribe")
    public ResponseEntity<UserResponseDto> unsubscribe(@PathVariable String username, @AuthenticationPrincipal User user) {
        User updatedUser = userService.unsubscribe(user, username);
        return ResponseEntity.ok(userMapper.userToDto(updatedUser));
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable String username) {
        userService.deleteUser(User.builder().username(username).build());
        return ResponseEntity.ok("User %s deleted".formatted(username));
    }
}
