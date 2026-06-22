package com.investkaro.dto;

import com.investkaro.entity.MeetingStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MeetingStatusRequest {
    @NotNull
    private MeetingStatus status;
}
