package com.investkaro.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class StartupUpdateRequest {
    @NotNull
    private Long startupId;

    @NotBlank
    private String title;

    @NotBlank
    private String description;
}
