package com.investkaro.service.Impl;

import com.investkaro.dto.RegisterRequest;
import com.investkaro.entity.FounderProfile;
import com.investkaro.entity.InvestorProfile;
import com.investkaro.entity.Role;
import com.investkaro.entity.User;
import com.investkaro.repository.FounderRepository;
import com.investkaro.repository.InvestorRepository;
import com.investkaro.repository.UserRepository;
import com.investkaro.security.JwtService;
import com.investkaro.service.AuthService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final FounderRepository founderRepository;
    private final InvestorRepository investorRepository;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, FounderRepository founderRepository, InvestorRepository investorRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.founderRepository = founderRepository;
        this.investorRepository = investorRepository;
    }

    @Override
    public User register(RegisterRequest request) {
        String email = request.getEmail() == null ? "" : request.getEmail().trim().toLowerCase();

        if (email.isBlank()) {
            throw new ResponseStatusException(BAD_REQUEST, "Email is required");
        }

        if (request.getPassword() == null || request.getPassword().trim().length() < 4) {
            throw new ResponseStatusException(BAD_REQUEST, "Password must be at least 4 characters");
        }

        if (request.getRole() == null) {
            throw new ResponseStatusException(BAD_REQUEST, "Role is required");
        }

        if (userRepository.existsByEmailIgnoreCase(email)) {
            throw new ResponseStatusException(CONFLICT, "An account with this email already exists");
        }

        User user = new User();
        user.setFullName(request.getFullName() == null ? "" : request.getFullName().trim());
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPhone(request.getPhone() == null ? "" : request.getPhone().trim());
        user.setLocation(request.getLocation() == null ? "" : request.getLocation().trim());
        user.setRole(request.getRole());

        User savedUser = userRepository.save(user);

        // Create founder profile automatically
        if (savedUser.getRole() == Role.FOUNDER) {

            FounderProfile founder = new FounderProfile();
            founder.setUser(savedUser);
            founderRepository.save(founder);
        }
        if (savedUser.getRole() == Role.INVESTOR) {

            InvestorProfile investor = new InvestorProfile();
            investor.setUser(savedUser);

            investorRepository.save(investor);
        }

        return savedUser;
    }

    @Override
    public String login(String email, String password) {
        String normalizedEmail = email == null ? "" : email.trim().toLowerCase();

        User user = userRepository.findByEmail(normalizedEmail)
                .orElseThrow(() -> new ResponseStatusException(UNAUTHORIZED, "Invalid email or password"));

        if(!passwordEncoder.matches(password , user.getPassword())){
            throw new ResponseStatusException(UNAUTHORIZED, "Invalid email or password");
        }
        return jwtService.generateToken(user.getEmail(),user.getRole().name());
    }
}
