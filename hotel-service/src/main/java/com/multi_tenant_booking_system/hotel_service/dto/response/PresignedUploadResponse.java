package com.multi_tenant_booking_system.hotel_service.dto.response;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PresignedUploadResponse {
  private String httpMethod;
  private String uploadUrl;
  private Map<String, String> headers;
  private String objectKey;
  private String publicUrl;
  private int expiresInSeconds;
}
