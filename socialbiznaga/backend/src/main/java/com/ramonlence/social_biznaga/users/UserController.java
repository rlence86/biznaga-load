package com.ramonlence.social_biznaga.users;

import com.ramonlence.social_biznaga.users.dto.LoginRequestDTO;
import com.ramonlence.social_biznaga.users.dto.LoginResultDTO;
import com.ramonlence.social_biznaga.users.dto.UserRegistrationDTO;
import com.ramonlence.social_biznaga.users.model.User;
import com.ramonlence.social_biznaga.users.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody @Valid UserRegistrationDTO userRegistrationDto) {
        User user = userService.register(userRegistrationDto);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResultDTO> loginUser(@RequestBody LoginRequestDTO loginRequestDto) {
        String token = userService.login(loginRequestDto);
        return ResponseEntity.ok(LoginResultDTO.builder().tokenId(token).build());
    }
}
