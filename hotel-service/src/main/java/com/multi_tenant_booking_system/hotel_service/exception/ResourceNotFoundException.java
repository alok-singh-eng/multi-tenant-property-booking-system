package com.multi_tenant_booking_system.hotel_service.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends HotelServiceException {

  public ResourceNotFoundException(String message) {
    super(HttpStatus.NOT_FOUND, message);
  }
}
