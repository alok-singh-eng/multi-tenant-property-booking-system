package com.multi_tenant_booking_system.user_service.utility;

public final class ApiMessages {

  private ApiMessages() {}

  public static final String ACCESS_DENIED = "You do not have permission to perform this action.";
  public static final String VALIDATION_FAILED = "Request validation failed.";
  public static final String MALFORMED_REQUEST_BODY = "Malformed request body.";
  public static final String UNAUTHENTICATED = "Authentication is required.";
  public static final String UNEXPECTED_SERVER_ERROR = "An unexpected error occurred.";
}
