package com.investkaro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkResponse {
    private Long id;
    private Long startupId;
    private String companyName;
    private String industryName;
    private Double fundingGoal;
    private LocalDateTime createdAt;
}
