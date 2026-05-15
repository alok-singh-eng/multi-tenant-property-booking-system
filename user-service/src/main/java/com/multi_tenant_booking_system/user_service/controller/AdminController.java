package com.multi_tenant_booking_system.user_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.multi_tenant_booking_system.user_service.dto.request.CreateAdminRequest;
import com.multi_tenant_booking_system.user_service.service.AdminService;
import com.multi_tenant_booking_system.user_service.utility.ApiPath;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(ApiPath.AUTH_BASE_URL)
@RequiredArgsConstructor
public class AdminController {

  private final AdminService adminService;

  @PostMapping(ApiPath.ADMIN)
  public ResponseEntity<String> createAdmin(@Valid @RequestBody CreateAdminRequest request) {
    adminService.createAdmin(request);
    return ResponseEntity.status(HttpStatus.CREATED).body("Admin created successfully");
  }
}
