package com.multi_tenant_booking_system.hotel_service.security;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.multi_tenant_booking_system.hotel_service.exception.HotelServiceException;

public final class SecurityUtils {

  private SecurityUtils() {}

  public static Optional<JwtAuthenticationPrincipal> currentPrincipal() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || !auth.isAuthenticated() || !(auth.getPrincipal() instanceof JwtAuthenticationPrincipal p)) {
      return Optional.empty();
    }
    return Optional.of(p);
  }

  public static JwtAuthenticationPrincipal requireAuthenticatedUser() {
    return currentPrincipal()
        .orElseThrow(() -> new HotelServiceException(HttpStatus.UNAUTHORIZED, "Authentication is required."));
  }
}
