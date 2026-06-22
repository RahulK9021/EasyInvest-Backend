package com.investkaro.service.Impl;

import com.investkaro.dto.ConversationRequest;
import com.investkaro.dto.ConversationResponse;
import com.investkaro.entity.*;
import com.investkaro.repository.*;
import com.investkaro.service.ConversationService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConversationServiceImpl implements ConversationService {
    private final ConversationRepository conversationRepository;
    private final MessageRepository messageRepository;
    private final StartupRepository startupRepository;
    private final InvestorRepository investorRepository;
    private final FounderRepository founderRepository;
    private final UserRepository userRepository;

    public ConversationServiceImpl(ConversationRepository conversationRepository, MessageRepository messageRepository, StartupRepository startupRepository, InvestorRepository investorRepository, FounderRepository founderRepository, UserRepository userRepository) {
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
        this.startupRepository = startupRepository;
        this.investorRepository = investorRepository;
        this.founderRepository = founderRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ConversationResponse createConversation(ConversationRequest request, String email) {
        User user = getUser(email);
        InvestorProfile investor = investorRepository.findByUser(user)
                .orElseThrow(() -> new EntityNotFoundException("Investor not found"));
        Startup startup = startupRepository.findById(request.getStartupId())
                .orElseThrow(() -> new EntityNotFoundException("Startup not found"));
        FounderProfile founder = startup.getFounder();
        Conversation conversation = conversationRepository.findByInvestorAndFounderAndStartup(investor, founder, startup).orElseGet(() -> {
            Conversation item = new Conversation();
            item.setInvestor(investor);
            item.setFounder(founder);
            item.setStartup(startup);
            return conversationRepository.save(item);
        });
        return toResponse(conversation, user);
    }

    @Override
    public List<ConversationResponse> getConversations(String email) {
        User user = getUser(email);
        if (user.getRole() == Role.INVESTOR) {
            InvestorProfile investor = investorRepository.findByUser(user)
                    .orElseThrow(() -> new EntityNotFoundException("Investor not found"));
            return conversationRepository.findByInvestorOrderByCreatedAtDesc(investor).stream()
                    .map(conversation -> toResponse(conversation, user))
                    .toList();
        }
        if (user.getRole() == Role.FOUNDER) {
            FounderProfile founder = founderRepository.findByUser(user)
                    .orElseThrow(() -> new EntityNotFoundException("Founder not found"));
            return conversationRepository.findByFounderOrderByCreatedAtDesc(founder).stream()
                    .map(conversation -> toResponse(conversation, user))
                    .toList();
        }
        throw new RuntimeException("Only founders and investors can view conversations");
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }

    private ConversationResponse toResponse(Conversation conversation, User currentUser) {
        return new ConversationResponse(
                conversation.getId(),
                conversation.getInvestor().getId(),
                conversation.getInvestor().getUser().getFullName(),
                conversation.getFounder().getId(),
                conversation.getFounder().getUser().getFullName(),
                conversation.getStartup().getId(),
                conversation.getStartup().getCompanyName(),
                conversation.getCreatedAt(),
                messageRepository.countByConversationAndSenderNotAndIsReadFalse(conversation, currentUser)
        );
    }
}
