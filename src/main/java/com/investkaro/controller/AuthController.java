package com.investkaro.controller;

import com.investkaro.dto.LoginRequest;
import com.investkaro.dto.LoginResponse;
import com.investkaro.dto.RegisterRequest;
import com.investkaro.entity.User;
import com.investkaro.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public User register(@RequestBody RegisterRequest request){
        return authService.register(request);
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request){
        String token = authService.login(request.getEmail() , request.getPassword());
        return new LoginResponse(token);
    }
}
