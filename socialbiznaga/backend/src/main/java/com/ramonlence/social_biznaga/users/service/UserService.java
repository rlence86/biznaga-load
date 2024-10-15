package com.ramonlence.social_biznaga.users.service;

import com.ramonlence.social_biznaga.users.dto.LoginRequestDTO;
import com.ramonlence.social_biznaga.users.dto.UserRegistrationDTO;
import com.ramonlence.social_biznaga.users.model.User;
import com.ramonlence.social_biznaga.users.repository.UserRepository;
import com.ramonlence.social_biznaga.utils.JWTUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    private JWTUtil jwtUtil;

    public User register(UserRegistrationDTO registrationDto) {
        Optional<User> existingUser = userRepository.findByUsername(registrationDto.getUsername());
        if (existingUser.isPresent()) {
            throw new IllegalStateException("Username already taken");
        }

        User user = User.builder()
                .username(registrationDto.getUsername())
                .password(passwordEncoder.encode((registrationDto.getPassword())))
                .email(registrationDto.getEmail())
                .build();
        return userRepository.save(user);
    }

    public String login(LoginRequestDTO loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));

        if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return jwtUtil.generateToken(user.getUsername(), user.getId());
        } else {
            throw new IllegalArgumentException("Invalid username or password");
        }
    }
}
