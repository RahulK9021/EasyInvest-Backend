package com.investkaro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FounderDashboardResponse {

    private Long startupId;
    private String companyName;
    private Double totalFunding;
    private Double amountRequired;
    private Long totalInvestors;
    private Double fundingPercentage;

}
