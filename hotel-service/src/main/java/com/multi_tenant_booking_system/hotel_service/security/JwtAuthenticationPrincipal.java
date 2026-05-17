package com.multi_tenant_booking_system.hotel_service.security;

import com.multi_tenant_booking_system.hotel_service.dto.Role;

public record JwtAuthenticationPrincipal(String email, Role role) {}
