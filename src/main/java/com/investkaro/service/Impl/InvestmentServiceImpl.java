package com.investkaro.service.Impl;

import com.investkaro.dto.InvestmentHistoryResponse;
import com.investkaro.dto.InvestorDashboardResponse;
import com.investkaro.entity.*;
import com.investkaro.repository.*;
import com.investkaro.service.InvestmentService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class InvestmentServiceImpl implements InvestmentService {
    private final InvestmentRepository investmentRepository;
    private final StartupRepository startupRepository;
    private final InvestorRepository investorRepository;
    private final UserRepository userRepository;
    private final FounderRepository founderRepository;

    public InvestmentServiceImpl(
            InvestmentRepository investmentRepository,
            StartupRepository startupRepository,
            InvestorRepository investorRepository,
            UserRepository userRepository, FounderRepository founderRepository) {

        this.investmentRepository = investmentRepository;
        this.startupRepository = startupRepository;
        this.investorRepository = investorRepository;
        this.userRepository = userRepository;
        this.founderRepository = founderRepository;
    }
    @Override
    public Map<String, Object> invest(Long startupId, Double amount, String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        InvestorProfile investor = investorRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Investor not found"));

        Startup startup = startupRepository.findById(startupId)
                .orElseThrow(() -> new RuntimeException("Startup not found"));

        boolean alreadyInvested =
                investmentRepository.existsByInvestor_IdAndStartup_Id(
                        investor.getId(),
                        startupId
                );

        if (alreadyInvested) {
            throw new RuntimeException("You already invested in this startup");
        }

        Investment investment = new Investment();
        investment.setAmount(amount);
        investment.setInvestor(investor);
        investment.setStartup(startup);
        investment.setCreatedAt(LocalDateTime.now());

        investmentRepository.save(investment);

        startup.setTotalFunding(startup.getTotalFunding() + amount);
        startupRepository.save(startup);

        return Map.of(
                "message", "Investment successful",
                "amount", amount,
                "startupId", startupId
        );
    }

    @Override
    public InvestorDashboardResponse getInvestorDashboard(Long investorId) {

            Double totalInvested = investmentRepository.getTotalInvested(investorId);

            Double largestInvestment = investmentRepository.getLargestInvestment(investorId);

            Long totalStartups = investmentRepository.getTotalStartupsInvested(investorId);

            return new InvestorDashboardResponse(
                    totalInvested != null ? totalInvested : 0,
                    totalStartups != null ? totalStartups : 0,
                    largestInvestment != null ? largestInvestment : 0
            );
        }

    @Override
    public List<InvestmentHistoryResponse> getInvestmentHistory(Long investorId) {
        List<Investment> investments =
                investmentRepository.findByInvestorIdOrderByCreatedAtDesc(investorId);
        return investments.stream()
                .map(investment -> new InvestmentHistoryResponse(
                        investment.getStartup().getCompanyName(),
                        investment.getAmount(),
                        investment.getCreatedAt()
                ))
                .toList();
    }
}


