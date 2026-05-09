package com.multi_tenant_booking_system.search_service.api;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/search")
public class ServiceHealthController {

	@GetMapping("/health")
	public Map<String, String> health() {
		return Map.of("status", "UP", "service", "search-service");
	}
}
