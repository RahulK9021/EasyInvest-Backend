package com.investkaro.controller;

import com.investkaro.dto.ConversationRequest;
import com.investkaro.service.ConversationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/conversations")
public class ConversationController {
    private final ConversationService conversationService;

    public ConversationController(ConversationService conversationService) {
        this.conversationService = conversationService;
    }

    @PostMapping
    public ResponseEntity<?> createConversation(@Valid @RequestBody ConversationRequest request, Authentication authentication) {
        return ResponseEntity.ok(conversationService.createConversation(request, authentication.getName()));
    }

    @GetMapping
    public ResponseEntity<?> getConversations(Authentication authentication) {
        return ResponseEntity.ok(conversationService.getConversations(authentication.getName()));
    }
}
