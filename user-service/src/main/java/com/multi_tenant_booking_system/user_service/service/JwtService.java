package com.multi_tenant_booking_system.user_service.service;

import java.util.Optional;

import com.multi_tenant_booking_system.user_service.entity.User;
import com.multi_tenant_booking_system.user_service.dto.JwtAuthenticationPrincipal;

public interface JwtService {
  String generateToken(User user);

  Optional<JwtAuthenticationPrincipal> parseAccessToken(String token);
}
