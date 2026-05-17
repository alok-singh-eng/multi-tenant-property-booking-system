package com.multi_tenant_booking_system.hotel_service.dto.response;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HotelResponse {
  private String id;
  private String name;
  private String location;
  private String description;
  private Instant createdAt;
  private Instant updatedAt;
}
