package com.multi_tenant_booking_system.user_service.service;

import com.multi_tenant_booking_system.user_service.entity.User;
import org.springframework.stereotype.Service;

@Service
public interface JwtService {
  String generateToken(User user);
  String extractUsername(String token);
}
