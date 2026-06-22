package com.investkaro.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @Column(length = 2000)
    private String content;

    private LocalDateTime timestamp;

    @Column(name = "is_read")
    private Boolean isRead = false;

    @PrePersist
    public void prePersist() {
        timestamp = LocalDateTime.now();
    }
}
