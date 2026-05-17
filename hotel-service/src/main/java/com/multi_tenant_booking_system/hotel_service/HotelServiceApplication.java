package com.multi_tenant_booking_system.hotel_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.multi_tenant_booking_system.hotel_service.config.GcsStorageProperties;

@SpringBootApplication
@EnableConfigurationProperties(GcsStorageProperties.class)
public class HotelServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(HotelServiceApplication.class, args);
	}

}
