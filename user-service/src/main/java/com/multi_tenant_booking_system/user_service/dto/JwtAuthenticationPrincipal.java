package com.multi_tenant_booking_system.user_service.dto;

public record JwtAuthenticationPrincipal(String email, Role role) {}
