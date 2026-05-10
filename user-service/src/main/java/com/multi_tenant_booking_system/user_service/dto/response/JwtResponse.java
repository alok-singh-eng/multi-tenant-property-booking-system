package com.multi_tenant_booking_system.user_service.dto.response;

import com.multi_tenant_booking_system.user_service.dto.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
  private String token;
  private String email;
  private Role role;
}
