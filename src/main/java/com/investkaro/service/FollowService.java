package com.investkaro.service;

import com.investkaro.dto.FollowResponse;

import java.util.List;

public interface FollowService {
    FollowResponse followStartup(Long startupId, String email);
    List<FollowResponse> getFollows(String email);
    void unfollowStartup(Long startupId, String email);
}
