package com.multi_tenant_booking_system.hotel_service.service;

import java.util.Optional;

import com.multi_tenant_booking_system.hotel_service.security.JwtAuthenticationPrincipal;

public interface JwtService {

  Optional<JwtAuthenticationPrincipal> parseAccessToken(String token);
}
