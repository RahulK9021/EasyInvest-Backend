//package com.investkaro.controller;
//
//import com.investkaro.repository.UserRepository;
//import com.investkaro.service.InvestmentService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
//import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
//
//import org.springframework.test.web.servlet.MockMvc;
//import java.util.Map;
//import static org.mockito.Mockito.when;
//import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import org.springframework.boot.test.mock.mockito.MockBean;
//
//@WebMvcTest(InvestmentController.class)
//@AutoConfigureMockMvc(addFilters = false)
//class InvestmentControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private InvestmentService investmentService;
//
//    @MockBean
//    private UserRepository userRepository;
//
//    @Test
//    void testInvestSuccess() throws Exception {
//
//        when(investmentService.invest(1L, 5000.0, "abc@gmail.com"))
//                .thenReturn(Map.of(
//                        "message", "Investment successful",
//                        "amount", 5000.0
//                ));
//
//        mockMvc.perform(post("/investor/1")
//                        .param("amount", "5000.0")
//                        .with(user("abc@gmail.com")))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").value("Investment successful"));
//    }
//}