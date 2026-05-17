package com.multi_tenant_booking_system.hotel_service.exception;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.multi_tenant_booking_system.hotel_service.dto.response.ApiErrorResponse;
import com.multi_tenant_booking_system.hotel_service.dto.response.ApiFieldViolation;
import com.multi_tenant_booking_system.hotel_service.utility.ApiMessages;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ApiErrorResponse> handleAccessDenied(AccessDeniedException ex) {
    log.warn("access denied: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.FORBIDDEN)
        .body(ApiErrorResponse.builder().message(ApiMessages.ACCESS_DENIED).build());
  }

  @ExceptionHandler(HotelServiceException.class)
  public ResponseEntity<ApiErrorResponse> handleDomain(HotelServiceException ex) {
    log.warn("domain error status={} message={}", ex.getHttpStatus().value(), ex.getMessage());
    return ResponseEntity.status(ex.getHttpStatus())
        .body(ApiErrorResponse.builder().message(ex.getMessage()).build());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
    List<ApiFieldViolation> violations = ex.getBindingResult().getFieldErrors().stream()
        .map(err -> ApiFieldViolation.builder()
            .field(err.getField())
            .rejectedValue(err.getRejectedValue() != null ? String.valueOf(err.getRejectedValue()) : null)
            .message(err.getDefaultMessage())
            .build())
        .toList();
    log.warn("validation failed: {}", violations);
    return ResponseEntity.badRequest()
        .body(ApiErrorResponse.builder().message(ApiMessages.VALIDATION_FAILED).violations(violations).build());
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ApiErrorResponse> handleNotReadable(HttpMessageNotReadableException ex) {
    log.warn("malformed JSON: {}", ex.getMessage());
    return ResponseEntity.badRequest()
        .body(ApiErrorResponse.builder().message(ApiMessages.MALFORMED_REQUEST_BODY).build());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiErrorResponse> handleGeneric(Exception ex, WebRequest webRequest) {
    log.error("unhandled failure path={}", webRequest.getDescription(false), ex);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ApiErrorResponse.builder().message(ApiMessages.UNEXPECTED_SERVER_ERROR).build());
  }
}
