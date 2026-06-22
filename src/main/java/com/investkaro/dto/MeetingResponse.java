package com.investkaro.dto;

import com.investkaro.entity.MeetingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MeetingResponse {
    private Long id;
    private Long investorId;
    private String investorName;
    private Long founderId;
    private String founderName;
    private Long startupId;
    private String startupName;
    private LocalDateTime meetingDate;
    private MeetingStatus status;
}
