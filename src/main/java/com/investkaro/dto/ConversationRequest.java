package com.investkaro.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ConversationRequest {
    @NotNull
    private Long startupId;
}
