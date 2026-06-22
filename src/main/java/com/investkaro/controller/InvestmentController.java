package com.investkaro.controller;

import com.investkaro.entity.InvestorProfile;
import com.investkaro.entity.User;
import com.investkaro.repository.InvestorRepository;
import com.investkaro.repository.UserRepository;
import com.investkaro.service.InvestmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/investor/investments")
public class InvestmentController {
    private final InvestmentService investmentService;
    private  final UserRepository userRepository;
    private final InvestorRepository investorRepository;

    public InvestmentController(InvestmentService investmentService, UserRepository userRepository, InvestorRepository investorRepository) {
        this.investmentService = investmentService;
        this.userRepository = userRepository;
        this.investorRepository = investorRepository;
    }

    @PostMapping("/{startupId}")
    public ResponseEntity<?> invest(
            @PathVariable Long startupId,
            @RequestParam Double amount,
            Authentication authentication
    ) {
        String email = authentication.getName();

        Map<String, Object> response =
                investmentService.invest(startupId, amount, email);

        return ResponseEntity.ok(response); // ✅ JSON response
    }

    @GetMapping("/history")
    public ResponseEntity<?> getInvestmentHistory(Authentication authentication) {

        User user = userRepository
                .findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        InvestorProfile investor = investorRepository
                .findByUser(user)
                .orElseThrow(()-> new RuntimeException("Investor not found"));

        return ResponseEntity.ok(
                investmentService.getInvestmentHistory(investor.getId())
        );
    }
}
