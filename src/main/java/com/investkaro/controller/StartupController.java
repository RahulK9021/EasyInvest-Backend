package com.investkaro.controller;

import com.investkaro.dto.FundingProgressDTO;
import com.investkaro.dto.StartupRequest;
import com.investkaro.dto.StartupResponse;
import com.investkaro.dto.UpdateStartupRequest;
import com.investkaro.entity.Startup;
import com.investkaro.service.InvestmentService;
import com.investkaro.service.StartupService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/startups")
public class StartupController {
    private final StartupService startupService;
    private final InvestmentService investmentService;

    public StartupController(StartupService startupService, InvestmentService investmentService) {
        this.startupService = startupService;
        this.investmentService = investmentService;
    }

    @PostMapping
    public Startup createStartup(@RequestBody StartupRequest request , Authentication authentication){
       String email = authentication.getName();
       return startupService.createStartup(request , email);
    }

    @GetMapping
    public List<StartupResponse> getStartups(Authentication authentication){
        if (authentication == null) {
            return startupService.getAllStartups();
        }
        return startupService.getAllStartups(authentication.getName());
    }

    @GetMapping("progress/{startupId}")
    @PreAuthorize("hasRole('FOUNDER') or hasRole('INVESTOR')")
    public FundingProgressDTO getFundingProgress(@PathVariable Long startupId){
        return startupService.getFundingProgress(startupId);
    }

    @GetMapping("/{startupId}/investors")
    public ResponseEntity<?> getStartupInvestors(@PathVariable Long startupId){
        return ResponseEntity.ok(startupService.getInvestors(startupId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateStartup(@PathVariable Long id , @RequestBody UpdateStartupRequest request , Authentication authentication){
        Startup updatedStartup = startupService.updateStartup( id , request , authentication.getName());
        return ResponseEntity.ok(updatedStartup);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getStartupById(@PathVariable Long id){
        Startup startup = startupService.findById(id).orElseThrow(()->new RuntimeException("Startup not found"));
        return ResponseEntity.ok(startup);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStartup(@PathVariable Long id , Authentication authentication){
        startupService.deleteStartup(id , authentication.getName());
        return ResponseEntity.ok("Deleted Successfully");
    }

    @GetMapping("/filter")
    public List<StartupResponse> filterStartups(
            @RequestParam(required = false) Long industryId,
            @RequestParam(required = false) Double minAmount,
            @RequestParam(required = false) Double maxAmount) {

        return startupService.filterStartups(industryId, minAmount, maxAmount);
    }

    @GetMapping("/dashboard")
    public ResponseEntity<?> getFounderDashboard(Authentication authentication) {

        return ResponseEntity.ok(
                startupService.getFounderDashboard(authentication.getName())
        );
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchStartups(
            @RequestParam String keyword) {

        return ResponseEntity.ok(
                startupService.searchStartups(keyword)
        );
    }
}
