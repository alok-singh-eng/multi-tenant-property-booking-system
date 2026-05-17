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
public class ReviewResponse {
  private String id;
  private String hotelId;
  private String authorEmail;
  private BigDecimal rating;
  private String comment;
  private Instant createdAt;
}
