package com.investkaro.controller;

import com.investkaro.dto.MeetingRequest;
import com.investkaro.dto.MeetingStatusRequest;
import com.investkaro.service.MeetingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/meetings")
public class MeetingController {
    private final MeetingService meetingService;

    public MeetingController(MeetingService meetingService) {
        this.meetingService = meetingService;
    }

    @PostMapping
    public ResponseEntity<?> requestMeeting(@Valid @RequestBody MeetingRequest request, Authentication authentication) {
        return ResponseEntity.ok(meetingService.requestMeeting(request, authentication.getName()));
    }

    @GetMapping
    public ResponseEntity<?> getMeetings(Authentication authentication) {
        return ResponseEntity.ok(meetingService.getMeetings(authentication.getName()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMeeting(@PathVariable Long id, @Valid @RequestBody MeetingStatusRequest request, Authentication authentication) {
        return ResponseEntity.ok(meetingService.updateMeeting(id, request, authentication.getName()));
    }
}
