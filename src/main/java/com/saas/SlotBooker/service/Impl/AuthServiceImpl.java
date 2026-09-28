package com.saas.SlotBooker.service.Impl;

import com.saas.SlotBooker.config.TokenConfig;
import com.saas.SlotBooker.domain.auth.LoginRequest;
import com.saas.SlotBooker.domain.auth.LoginResponse;
import com.saas.SlotBooker.domain.auth.RegisterUserRequest;
import com.saas.SlotBooker.domain.auth.RegisterUserResponse;
import com.saas.SlotBooker.infra.enums.Role;
import com.saas.SlotBooker.model.User;
import com.saas.SlotBooker.repository.UserRepository;
import com.saas.SlotBooker.service.IAuthService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements IAuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenConfig tokenConfig;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, TokenConfig tokenConfig) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenConfig = tokenConfig;
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        UsernamePasswordAuthenticationToken userAndPass = new UsernamePasswordAuthenticationToken(request.email(), request.password());
        Authentication authentication = authenticationManager.authenticate(userAndPass);

        User user = (User) authentication.getPrincipal();
        String token = tokenConfig.generateToken(user);
        return new LoginResponse(token);
    }

    @Override
    public RegisterUserResponse registerUser(RegisterUserRequest request) {
        User newUser = new User();
        newUser.setPassword(passwordEncoder.encode(request.password()));
        newUser.setEmail(request.email());
        newUser.setName(request.name());
        newUser.setPhone(request.phone());
        newUser.setRole(Role.PROVIDER);

        userRepository.save(newUser);
        return new RegisterUserResponse(newUser.getName(), newUser.getEmail(), "dummy-token");
    }
}
