package com.multi_tenant_booking_system.user_service.exception;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.multi_tenant_booking_system.user_service.dto.response.ApiErrorResponse;
import com.multi_tenant_booking_system.user_service.dto.response.ApiFieldViolation;
import com.multi_tenant_booking_system.user_service.utility.ApiMessages;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ApiErrorResponse> handleAccessDenied(AccessDeniedException ex) {
    log.warn("access denied: {}", ex.getMessage());
    ApiErrorResponse body =
        ApiErrorResponse.builder().message(ApiMessages.ACCESS_DENIED).build();
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body);
  }

  @ExceptionHandler(UserServiceException.class)
  public ResponseEntity<ApiErrorResponse> handleUserService(UserServiceException ex) {
    log.warn("domain error status={} message={}", ex.getHttpStatus().value(), ex.getMessage());
    ApiErrorResponse body = ApiErrorResponse.builder().message(ex.getMessage()).build();
    return ResponseEntity.status(ex.getHttpStatus()).body(body);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
    List<ApiFieldViolation> violations = ex.getBindingResult().getFieldErrors().stream()
        .map(err -> ApiFieldViolation.builder()
            .field(err.getField())
            .rejectedValue(err.getRejectedValue() != null ? String.valueOf(err.getRejectedValue())
                : null)
            .message(err.getDefaultMessage())
            .build())
        .toList();

    log.warn("validation failed: {}", violations);

    ApiErrorResponse body = ApiErrorResponse.builder()
        .message(ApiMessages.VALIDATION_FAILED)
        .violations(violations)
        .build();
    return ResponseEntity.badRequest().body(body);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ApiErrorResponse> handleNotReadable(HttpMessageNotReadableException ex) {
    log.warn("malformed JSON: {}", ex.getMessage());
    ApiErrorResponse body =
        ApiErrorResponse.builder().message(ApiMessages.MALFORMED_REQUEST_BODY).build();
    return ResponseEntity.badRequest().body(body);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiErrorResponse> handleGeneric(Exception ex, WebRequest webRequest) {
    log.error("unhandled failure path={}", webRequest.getDescription(false), ex);
    ApiErrorResponse body = ApiErrorResponse.builder()
        .message(ApiMessages.UNEXPECTED_SERVER_ERROR)
        .build();
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
  }
}
