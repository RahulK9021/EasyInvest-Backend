package com.investkaro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowResponse {
    private Long id;
    private Long startupId;
    private String companyName;
    private String industryName;
    private Double fundingGoal;
}
