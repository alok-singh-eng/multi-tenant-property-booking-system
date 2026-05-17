package com.multi_tenant_booking_system.hotel_service.entity;

import java.math.BigDecimal;
import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "reviews")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Review {

  @Id
  private String id;
  private String hotelId;
  /** Email from JWT subject (same issuer as user-service). */
  private String authorEmail;
  private BigDecimal rating;
  private String comment;
  private Instant createdAt;
}
