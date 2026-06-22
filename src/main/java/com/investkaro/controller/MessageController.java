package com.investkaro.controller;

import com.investkaro.dto.MessageRequest;
import com.investkaro.service.MessageService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/messages")
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping
    public ResponseEntity<?> sendMessage(@Valid @RequestBody MessageRequest request, Authentication authentication) {
        return ResponseEntity.ok(messageService.sendMessage(request, authentication.getName()));
    }

    @GetMapping("/{conversationId}")
    public ResponseEntity<?> getMessages(@PathVariable Long conversationId, Authentication authentication) {
        return ResponseEntity.ok(messageService.getMessages(conversationId, authentication.getName()));
    }
}
