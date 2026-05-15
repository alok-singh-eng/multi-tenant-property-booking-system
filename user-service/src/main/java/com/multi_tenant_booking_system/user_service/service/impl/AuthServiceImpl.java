package com.multi_tenant_booking_system.user_service.service.impl;

import com.multi_tenant_booking_system.user_service.dto.Role;
import com.multi_tenant_booking_system.user_service.dto.request.LoginRequest;
import com.multi_tenant_booking_system.user_service.dto.request.SignupRequest;
import com.multi_tenant_booking_system.user_service.dto.response.JwtResponse;
import com.multi_tenant_booking_system.user_service.entity.User;
import com.multi_tenant_booking_system.user_service.exception.DuplicateEmailException;
import com.multi_tenant_booking_system.user_service.exception.InvalidCredentialsException;
import com.multi_tenant_booking_system.user_service.repository.UserRepository;
import com.multi_tenant_booking_system.user_service.service.AuthService;
import com.multi_tenant_booking_system.user_service.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final JwtService jwtService;
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public void signUp(SignupRequest request) {
    if (userRepository.existsByEmail(request.getEmail())) {
      throw new DuplicateEmailException("An account with this email already exists.");
    }

    User user = User.builder().name(request.getName()).email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword())).role(Role.USER).build();

    userRepository.save(user);
  }

  @Override
  public JwtResponse signIn(LoginRequest request) {
    User user = userRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password."));
    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
      throw new InvalidCredentialsException("Invalid email or password.");
    }
    String token = jwtService.generateToken(user);
    return JwtResponse.builder().token(token).email(user.getEmail()).role(user.getRole()).build();
  }
}
