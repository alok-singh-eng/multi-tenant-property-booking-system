package com.multi_tenant_booking_system.user_service.controller;

import com.multi_tenant_booking_system.user_service.dto.request.LoginRequest;
import com.multi_tenant_booking_system.user_service.dto.request.SignupRequest;
import com.multi_tenant_booking_system.user_service.dto.response.JwtResponse;
import com.multi_tenant_booking_system.user_service.service.AuthService;
import com.multi_tenant_booking_system.user_service.utility.ApiPath;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiPath.AUTH_BASE_URL)
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping(ApiPath.SIGN_UP)
  public ResponseEntity<String> signUp(
      @RequestBody SignupRequest request) {

    authService.signUp(request);

    return ResponseEntity.ok("User registered successfully");
  }

  @PostMapping(ApiPath.SIGN_IN)
  public ResponseEntity<JwtResponse> signIn(
      @RequestBody LoginRequest request) {

    return ResponseEntity.ok(
        authService.signIn(request)
    );
  }
}
