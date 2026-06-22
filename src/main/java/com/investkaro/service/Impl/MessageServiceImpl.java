package com.investkaro.service.Impl;

import com.investkaro.dto.MessageRequest;
import com.investkaro.dto.MessageResponse;
import com.investkaro.entity.*;
import com.investkaro.repository.ConversationRepository;
import com.investkaro.repository.MessageRepository;
import com.investkaro.repository.UserRepository;
import com.investkaro.service.MessageService;
import com.investkaro.service.NotificationService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public MessageServiceImpl(MessageRepository messageRepository, ConversationRepository conversationRepository, UserRepository userRepository, NotificationService notificationService) {
        this.messageRepository = messageRepository;
        this.conversationRepository = conversationRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    @Override
    public MessageResponse sendMessage(MessageRequest request, String email) {
        User sender = getUser(email);
        Conversation conversation = conversationRepository.findById(request.getConversationId())
                .orElseThrow(() -> new EntityNotFoundException("Conversation not found"));
        ensureParticipant(conversation, sender);

        Message message = new Message();
        message.setConversation(conversation);
        message.setSender(sender);
        message.setContent(request.getContent());
        message.setIsRead(false);
        Message saved = messageRepository.save(message);

        User recipient = conversation.getInvestor().getUser().getId().equals(sender.getId())
                ? conversation.getFounder().getUser()
                : conversation.getInvestor().getUser();
        notificationService.createNotification(
                recipient,
                "New message",
                sender.getFullName() + " sent a message about " + conversation.getStartup().getCompanyName(),
                NotificationType.MESSAGE
        );

        return toResponse(saved);
    }

    @Transactional
    @Override
    public List<MessageResponse> getMessages(Long conversationId, String email) {
        User user = getUser(email);
        Conversation conversation = conversationRepository.findById(conversationId)
                .orElseThrow(() -> new EntityNotFoundException("Conversation not found"));
        ensureParticipant(conversation, user);
        List<Message> unreadMessages = messageRepository.findByConversationAndSenderNotAndIsReadFalse(conversation, user);
        unreadMessages.forEach(message -> message.setIsRead(true));
        return messageRepository.findByConversationOrderByTimestampAsc(conversation).stream().map(this::toResponse).toList();
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    private void ensureParticipant(Conversation conversation, User user) {
        boolean isInvestor = conversation.getInvestor().getUser().getId().equals(user.getId());
        boolean isFounder = conversation.getFounder().getUser().getId().equals(user.getId());
        if (!isInvestor && !isFounder) {
            throw new RuntimeException("You are not allowed to access this conversation");
        }
    }

    private MessageResponse toResponse(Message message) {
        return new MessageResponse(
                message.getId(),
                message.getConversation().getId(),
                message.getSender().getId(),
                message.getSender().getFullName(),
                message.getContent(),
                message.getTimestamp(),
                message.getIsRead()
        );
    }
}
