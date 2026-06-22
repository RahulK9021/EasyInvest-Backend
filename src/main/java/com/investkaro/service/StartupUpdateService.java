package com.investkaro.service;

import com.investkaro.dto.StartupUpdateRequest;
import com.investkaro.dto.StartupUpdateResponse;

import java.util.List;

public interface StartupUpdateService {
    StartupUpdateResponse createUpdate(StartupUpdateRequest request, String email);
    List<StartupUpdateResponse> getUpdates(Long startupId);
}
