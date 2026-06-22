package com.investkaro.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MeetingRequest {
    @NotNull
    private Long startupId;

    @NotNull
    @Future
    private LocalDateTime meetingDate;
}
