package com.investkaro.controller;

import com.investkaro.dto.StartupUpdateRequest;
import com.investkaro.service.StartupUpdateService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/startup-updates")
public class StartupUpdateController {
    private final StartupUpdateService startupUpdateService;

    public StartupUpdateController(StartupUpdateService startupUpdateService) {
        this.startupUpdateService = startupUpdateService;
    }

    @PostMapping
    public ResponseEntity<?> createUpdate(@Valid @RequestBody StartupUpdateRequest request, Authentication authentication) {
        return ResponseEntity.ok(startupUpdateService.createUpdate(request, authentication.getName()));
    }

    @GetMapping("/{startupId}")
    public ResponseEntity<?> getUpdates(@PathVariable Long startupId) {
        return ResponseEntity.ok(startupUpdateService.getUpdates(startupId));
    }
}
