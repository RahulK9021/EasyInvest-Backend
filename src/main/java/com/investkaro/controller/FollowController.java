package com.investkaro.controller;

import com.investkaro.service.FollowService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/follow")
public class FollowController {
    private final FollowService followService;

    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    @PostMapping("/{startupId}")
    public ResponseEntity<?> followStartup(@PathVariable Long startupId, Authentication authentication) {
        return ResponseEntity.ok(followService.followStartup(startupId, authentication.getName()));
    }

    @GetMapping
    public ResponseEntity<?> getFollows(Authentication authentication) {
        return ResponseEntity.ok(followService.getFollows(authentication.getName()));
    }

    @DeleteMapping("/{startupId}")
    public ResponseEntity<?> unfollowStartup(@PathVariable Long startupId, Authentication authentication) {
        followService.unfollowStartup(startupId, authentication.getName());
        return ResponseEntity.ok("Startup unfollowed");
    }
}
