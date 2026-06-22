package com.investkaro.service;

import com.investkaro.dto.MeetingRequest;
import com.investkaro.dto.MeetingResponse;
import com.investkaro.dto.MeetingStatusRequest;

import java.util.List;

public interface MeetingService {
    MeetingResponse requestMeeting(MeetingRequest request, String email);
    List<MeetingResponse> getMeetings(String email);
    MeetingResponse updateMeeting(Long id, MeetingStatusRequest request, String email);
}
