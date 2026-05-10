package com.multi_tenant_booking_system.user_service.service;

import com.multi_tenant_booking_system.user_service.dto.request.LoginRequest;
import com.multi_tenant_booking_system.user_service.dto.request.SignupRequest;
import com.multi_tenant_booking_system.user_service.dto.response.JwtResponse;

public interface AuthService {
  void signUp(SignupRequest request);
  JwtResponse signIn(LoginRequest request);
}
