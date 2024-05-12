package ua.oleksii.shchetinin.ps.demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.oleksii.shchetinin.ps.demo.dto.responce.FeedDto;
import ua.oleksii.shchetinin.ps.demo.model.User;
import ua.oleksii.shchetinin.ps.demo.service.UserService;

@RestController
@RequestMapping("/v1/feed")
@RequiredArgsConstructor
public class FeedController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<FeedDto<FeedDto.FullPost>> getMyFeed(@AuthenticationPrincipal User user) {
        var feed = userService.getExtendedFeed(user);
        return ResponseEntity.ok(feed);
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> getOtherFeed(@PathVariable String username, @AuthenticationPrincipal User user) {
        if(username.equals(user.getUsername())){
            return getMyFeed(user);
        }
        var feed = userService.getFeed(User.builder().username(username).build());
        return ResponseEntity.ok(feed);
    }
}
