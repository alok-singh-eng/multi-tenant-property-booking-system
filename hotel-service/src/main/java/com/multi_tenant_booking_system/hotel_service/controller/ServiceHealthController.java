package com.multi_tenant_booking_system.hotel_service.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.multi_tenant_booking_system.hotel_service.utility.ApiPath;

@RestController
@RequestMapping(ApiPath.HOTEL_BASE)
public class ServiceHealthController {

  @GetMapping(ApiPath.HEALTH)
  public ResponseEntity<Map<String, String>> health() {
    return ResponseEntity.ok(Map.of("status", "UP", "service", "hotel-service"));
  }
}
