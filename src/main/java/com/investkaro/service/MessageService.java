package com.investkaro.service;

import com.investkaro.dto.MessageRequest;
import com.investkaro.dto.MessageResponse;

import java.util.List;

public interface MessageService {
    MessageResponse sendMessage(MessageRequest request, String email);
    List<MessageResponse> getMessages(Long conversationId, String email);
}
