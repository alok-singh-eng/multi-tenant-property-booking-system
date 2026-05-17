package com.multi_tenant_booking_system.hotel_service.dto.response;

import java.math.BigDecimal;
import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoomResponse {
  private String id;
  private String hotelId;
  private String roomType;
  private int capacity;
  private BigDecimal basePrice;
  private Instant createdAt;
  private Instant updatedAt;
}
