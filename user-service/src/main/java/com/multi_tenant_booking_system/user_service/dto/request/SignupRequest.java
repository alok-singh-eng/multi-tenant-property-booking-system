package com.multi_tenant_booking_system.user_service.dto.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SignupRequest {
  private String name;
  private String email;
  private String password;
}
