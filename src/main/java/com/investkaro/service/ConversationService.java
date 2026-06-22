package com.investkaro.service;

import com.investkaro.dto.ConversationRequest;
import com.investkaro.dto.ConversationResponse;

import java.util.List;

public interface ConversationService {
    ConversationResponse createConversation(ConversationRequest request, String email);
    List<ConversationResponse> getConversations(String email);
}
