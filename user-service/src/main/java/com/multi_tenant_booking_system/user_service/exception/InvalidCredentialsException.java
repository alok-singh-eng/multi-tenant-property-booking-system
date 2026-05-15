package com.multi_tenant_booking_system.user_service.exception;

import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends UserServiceException {

  public InvalidCredentialsException(String message) {
    super(HttpStatus.UNAUTHORIZED, message);
  }
}
