package com.investkaro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversationResponse {
    private Long id;
    private Long investorId;
    private String investorName;
    private Long founderId;
    private String founderName;
    private Long startupId;
    private String startupName;
    private LocalDateTime createdAt;
    private long unreadCount;
}
