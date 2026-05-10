package com.multi_tenant_booking_system.user_service.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginRequest {
  private String email;
  private String password;
}
