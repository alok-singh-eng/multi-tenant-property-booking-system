package com.multi_tenant_booking_system.hotel_service.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class HotelServiceException extends RuntimeException {

  private final HttpStatus httpStatus;

  public HotelServiceException(HttpStatus httpStatus, String message) {
    super(message);
    this.httpStatus = httpStatus;
  }
}
