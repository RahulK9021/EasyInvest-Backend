package com.investkaro.service.Impl;

import com.investkaro.dto.*;
import com.investkaro.entity.*;
import com.investkaro.repository.*;
import com.investkaro.service.StartupService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StartupServiceImpl implements StartupService {
    private final StartupRepository startupRepository;
    private final UserRepository userRepository;
    private final FounderRepository founderRepository;
    private final IndustryRepository industryRepository;
    private final InvestmentRepository investmentRepository;
    private final InterestRepository interestRepository;
    private final BookmarkRepository bookmarkRepository;
    private final FollowRepository followRepository;
    private final StartupUpdateRepository startupUpdateRepository;
    private final DocumentRepository documentRepository;
    private final MeetingRepository meetingRepository;
    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;

    public StartupServiceImpl(StartupRepository startupRepository, UserRepository userRepository, FounderRepository founderRepository, IndustryRepository industryRepository, InvestmentRepository investmentRepository, InterestRepository interestRepository, BookmarkRepository bookmarkRepository, FollowRepository followRepository, StartupUpdateRepository startupUpdateRepository, DocumentRepository documentRepository, MeetingRepository meetingRepository, MessageRepository messageRepository, ConversationRepository conversationRepository) {
        this.startupRepository = startupRepository;
        this.userRepository = userRepository;
        this.founderRepository = founderRepository;
        this.industryRepository = industryRepository;
        this.investmentRepository = investmentRepository;
        this.interestRepository = interestRepository;
        this.bookmarkRepository = bookmarkRepository;
        this.followRepository = followRepository;
        this.startupUpdateRepository = startupUpdateRepository;
        this.documentRepository = documentRepository;
        this.meetingRepository = meetingRepository;
        this.messageRepository = messageRepository;
        this.conversationRepository = conversationRepository;
    }


    @Override
    public Startup createStartup(StartupRequest request, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        FounderProfile founder = founderRepository.findByUser(user).orElseThrow(() ->new RuntimeException("Founder Not found"));
        Industry industry = industryRepository.findById(request.getIndustryId()).orElseThrow(()-> new RuntimeException("Industry not found"));
        Startup startup = new Startup();
        startup.setCompanyName(request.getCompanyName());
        startup.setBusinessModel(request.getBusinessModel());
        startup.setFoundedYear(request.getFoundedYear());
        startup.setTeamSize(request.getTeamSize());
        startup.setProblem(request.getProblem());
        startup.setSolution(request.getSolution());
        startup.setTargetMarket(request.getTargetMarket());
        startup.setUsp(request.getUsp());
        startup.setCompetitors(request.getCompetitors());
        startup.setRevenue(request.getRevenue());
        startup.setBurnRate(request.getBurnRate());
        startup.setCac(request.getCac());
        startup.setLtv(request.getLtv());
        startup.setTotalFunding(request.getTotalFunding());
        startup.setAmountRequired(request.getAmountRequired());
        startup.setEquityOffered(request.getEquityOffered());
        startup.setValuation(request.getValuation());

        startup.setFounder(founder);
        startup.setIndustry(industry);

        return startupRepository.save(startup);
    }

    @Override
    public List<StartupResponse> getAllStartups(String email) {

        List<Startup> startups = startupRepository.findAll();

        return startups.stream()
                .map(startup -> new StartupResponse(
                        startup.getId(),
                        startup.getCompanyName(),
                        startup.getBusinessModel(),
                        startup.getFoundedYear(),
                        startup.getTeamSize(),
                        startup.getProblem(),
                        startup.getSolution(),
                        startup.getTargetMarket(),
                        startup.getUsp(),
                        startup.getCompetitors(),
                        startup.getRevenue(),
                        startup.getBurnRate(),
                        startup.getCac(),
                        startup.getLtv(),
                        startup.getTotalFunding(),
                        startup.getAmountRequired(),
                        startup.getEquityOffered(),
                        startup.getValuation(),
                        startup.getFounder().getUser().getFullName(),
                        startup.getIndustry().getName()
                ))
                .toList();
    }

    @Override
    public List<StartupResponse> getAllStartups() {

        List<Startup> startups = startupRepository.findAll();

        return startups.stream()
                .map(startup -> new StartupResponse(
                        startup.getId(),
                        startup.getCompanyName(),
                        startup.getBusinessModel(),
                        startup.getFoundedYear(),
                        startup.getTeamSize(),
                        startup.getProblem(),
                        startup.getSolution(),
                        startup.getTargetMarket(),
                        startup.getUsp(),
                        startup.getCompetitors(),
                        startup.getRevenue(),
                        startup.getBurnRate(),
                        startup.getCac(),
                        startup.getLtv(),
                        startup.getTotalFunding(),
                        startup.getAmountRequired(),
                        startup.getEquityOffered(),
                        startup.getValuation(),
                        startup.getFounder().getUser().getFullName(),
                        startup.getIndustry().getName()
                ))
                .toList();
    }

    @Override
    public List<StartupResponse> findByIndustry_Id(Long industryId) {
        if (!industryRepository.existsById(industryId)) {
            throw new EntityNotFoundException("Industry not found with id: " + industryId);
        }
        return startupRepository.findByIndustry_Id(industryId);
    }

    @Override
    public List<StartupResponse> findByInvestmentRange(Double min, Double max) {
        return startupRepository.findByAmountRequiredBetween(min , max);
    }

    @Override
    public List<StartupResponse> findByIndustry_IdAndAmountRequiredBetween(Long industryId, Double min, Double max) {
        return startupRepository.findByIndustry_IdAndAmountRequiredBetween(industryId , min , max);
    }

    @Override
    public FundingProgressDTO getFundingProgress(Long startupId) {
        Startup startup = startupRepository.findById(startupId).orElseThrow(()-> new RuntimeException("Startup not found"));
        Double raised = investmentRepository.getTotalInvestmentForStartup(startupId);
        if (raised == null){
            raised = 0.0;
        }

        Double required =  startup.getAmountRequired();

        Double progress = (raised / required) * 100 ;

        FundingProgressDTO dto = new FundingProgressDTO();

        dto.setStartupId(startupId);
        dto.setAmountRequired(required);
        dto.setAmountRaised(raised);
        dto.setProgressPercentage(progress);

        return dto;
    }

    @Override
    public List<TopStartupResponse> getTopFundedStartups() {
        List<Startup> startups = startupRepository.findTopFundedStartups();
        return startups.stream()
                .map(startup -> {
                    double percentage = 0;
                    if (startup.getAmountRequired() != null && startup.getAmountRequired() > 0){
                        Double funding = startup.getTotalFunding();
                        double safeFunding = (funding != null) ? funding : 0.0;
                        percentage = (safeFunding / startup.getAmountRequired()) * 100;
                    }

                    return new TopStartupResponse(
                            startup.getCompanyName(),
                            startup.getTotalFunding(),
                            startup.getAmountRequired(),
                            percentage
                    );
                }).toList();
    }

    @Override
    public List<StartupInvestorResponse> getInvestors(Long startupId) {
        List<Investment> investments = investmentRepository.findByStartup_Id(startupId);
        return investments.stream()
                .map(investment -> new StartupInvestorResponse(
                        investment.getInvestor().getUser().getFullName(),
                        investment.getAmount()
                ))
                .toList();
    }

    @Override
    public Startup updateStartup(Long id, UpdateStartupRequest request, String email) {
        Startup startup = startupRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found "));
        if (!startup.getFounder().getUser().getEmail().equals(email)){
            throw new RuntimeException("You are not allowed to update");
        }
        startup.setCompanyName(request.getCompanyName());
        startup.setBusinessModel(request.getBusinessModel());
        startup.setTeamSize(request.getTeamSize());
        startup.setProblem(request.getProblem());
        startup.setSolution(request.getSolution());
        startup.setTargetMarket(request.getTargetMarket());
        startup.setUsp(request.getUsp());
        startup.setCompetitors(request.getCompetitors());

        startup.setRevenue(request.getRevenue());
        startup.setBurnRate(request.getBurnRate());
        startup.setCac(request.getCac());
        startup.setLtv(request.getLtv());
        startup.setTotalFunding(request.getTotalFunding());

        startup.setAmountRequired(request.getAmountRequired());
        startup.setEquityOffered(request.getEquityOffered());
        startup.setValuation(request.getValuation());

        return startupRepository.save(startup);
    }

    @Transactional
    @Override
    public void deleteStartup(Long id ,String email) {
        Startup startup = startupRepository.findById(id).orElseThrow(() -> new RuntimeException("User no Found"));
        if (!startup.getFounder().getUser().getEmail().equals(email)){
            throw new RuntimeException("You are not allowed to delete");
        }
        interestRepository.deleteByStartupId(id);
        investmentRepository.deleteByStartupId(id);
        bookmarkRepository.deleteByStartupId(id);
        followRepository.deleteByStartupId(id);
        startupUpdateRepository.deleteByStartupId(id);
        documentRepository.deleteByStartupId(id);
        meetingRepository.deleteByStartupId(id);
        messageRepository.deleteByConversation_Startup_Id(id);
        conversationRepository.deleteByStartupId(id);
        startupRepository.delete(startup);
    }

    public List<StartupResponse> filterStartups(
            Long industryId,
            Double minAmount,
            Double maxAmount) {

        return startupRepository.filterStartups(industryId, minAmount, maxAmount);
    }

    public List<FounderDashboardResponse> getFounderDashboard(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        FounderProfile founder = founderRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Founder not found"));

        List<Startup> startups = startupRepository.findByFounder(founder);

        return startups.stream().map(startup -> {

            Long totalInvestors =
                    investmentRepository.countInvestorsByStartupId(startup.getId());

            double percentage = 0;

            if (startup.getAmountRequired() != null && startup.getAmountRequired() > 0) {
                percentage =
                        (startup.getTotalFunding() / startup.getAmountRequired()) * 100;
            }

            return new FounderDashboardResponse(
                    startup.getId(),
                    startup.getCompanyName(),
                    startup.getTotalFunding(),
                    startup.getAmountRequired(),
                    totalInvestors,
                    percentage
            );

        }).toList();
    }

    @Override
    public Optional<Startup> findById(Long id) {
        return startupRepository.findById(id);
    }


    public List<StartupResponse> searchStartups(String keyword) {
        return startupRepository.searchStartups(keyword);
    }


}
