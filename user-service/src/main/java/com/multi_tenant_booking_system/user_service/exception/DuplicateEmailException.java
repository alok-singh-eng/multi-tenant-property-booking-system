package com.multi_tenant_booking_system.user_service.exception;

import org.springframework.http.HttpStatus;

public class DuplicateEmailException extends UserServiceException {

  public DuplicateEmailException(String message) {
    super(HttpStatus.CONFLICT, message);
  }
}
