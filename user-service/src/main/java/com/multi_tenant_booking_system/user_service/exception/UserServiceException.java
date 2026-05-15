package com.multi_tenant_booking_system.user_service.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class UserServiceException extends RuntimeException {

  private final HttpStatus httpStatus;

  public UserServiceException(HttpStatus httpStatus, String message) {
    super(message);
    this.httpStatus = httpStatus;
  }
}
