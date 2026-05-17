package com.multi_tenant_booking_system.hotel_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApiFieldViolation {
  private String field;
  private String rejectedValue;
  private String message;
}
