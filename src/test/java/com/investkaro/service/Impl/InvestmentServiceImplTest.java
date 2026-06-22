package com.investkaro.service.Impl;

import com.investkaro.entity.InvestorProfile;
import com.investkaro.entity.Startup;
import com.investkaro.entity.User;
import com.investkaro.repository.InvestmentRepository;
import com.investkaro.repository.InvestorRepository;
import com.investkaro.repository.StartupRepository;
import com.investkaro.repository.UserRepository;
import com.investkaro.service.InvestmentService;
import io.jsonwebtoken.JwsHeader;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvestmentServiceImplTest {

    @Mock
    InvestmentRepository investmentRepository;

    @Mock
    InvestorRepository investorRepository;

    @Mock
    StartupRepository startupRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    InvestmentServiceImpl investmentService;

    @Test
    void testInvestSuccess() {

        String email = "abc@gmail.com";
        Long startupId = 1L;
        Double amount = 5000.0;

        User user = new User();
        InvestorProfile investorProfile = new InvestorProfile();
        investorProfile.setId(10L);

        Startup startup = new Startup();
        startup.setId(startupId);
        startup.setTotalFunding(100.0);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(investorRepository.findByUser(user)).thenReturn(Optional.of(investorProfile));
        when(startupRepository.findById(startupId)).thenReturn(Optional.of(startup));
        when(investmentRepository.existsByInvestor_IdAndStartup_Id(10L , startupId)).thenReturn(false);
        Map<String , Object> result = investmentService.invest(startupId , amount ,email);
        assertTrue(result.get("message").toString().contains("successful"));
        assertEquals(amount , result.get("amount"));

        verify(investmentRepository).save(any());
        verify(startupRepository).save(startup);

    }

    @Test
    void testAlreadyInvested() {

        String email = "abc@gmail.com";
        Long startupId = 1L;

        User user = new User();
        InvestorProfile investor = new InvestorProfile();
        investor.setId(10L);

        Startup startup = new Startup();

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        when(investorRepository.findByUser(user))
                .thenReturn(Optional.of(investor));

        when(startupRepository.findById(startupId))
                .thenReturn(Optional.of(startup));

        when(investmentRepository.existsByInvestor_IdAndStartup_Id(10L, startupId))
                .thenReturn(true);

        // Act + Assert
        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> investmentService.invest(startupId, 5000.0, email)
        );

        assertEquals("You already invested in this startup", ex.getMessage());

        // ❌ save should NOT be called
        verify(investmentRepository, never()).save(any());
    }
}