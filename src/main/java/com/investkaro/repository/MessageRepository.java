package com.investkaro.repository;

import com.investkaro.entity.Conversation;
import com.investkaro.entity.Message;
import com.investkaro.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByConversationOrderByTimestampAsc(Conversation conversation);
    long countByConversationAndSenderNotAndIsReadFalse(Conversation conversation, User sender);
    List<Message> findByConversationAndSenderNotAndIsReadFalse(Conversation conversation, User sender);
    void deleteByConversation_Startup_Id(Long startupId);
}
