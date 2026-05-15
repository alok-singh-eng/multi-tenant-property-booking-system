package com.multi_tenant_booking_system.user_service.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.multi_tenant_booking_system.user_service.dto.Role;
import com.multi_tenant_booking_system.user_service.dto.request.CreateAdminRequest;
import com.multi_tenant_booking_system.user_service.entity.User;
import com.multi_tenant_booking_system.user_service.exception.DuplicateEmailException;
import com.multi_tenant_booking_system.user_service.repository.UserRepository;
import com.multi_tenant_booking_system.user_service.service.AdminService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public void createAdmin(CreateAdminRequest request) {
    if (userRepository.existsByEmail(request.getEmail())) {
      throw new DuplicateEmailException("An account with this email already exists.");
    }

    User admin = User.builder()
        .name(request.getName())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .role(Role.ADMIN)
        .build();
    userRepository.save(admin);
  }
}
