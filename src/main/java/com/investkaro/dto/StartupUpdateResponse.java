package com.investkaro.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StartupUpdateResponse {
    private Long id;
    private Long startupId;
    private String startupName;
    private String title;
    private String description;
    private LocalDateTime createdAt;
}
