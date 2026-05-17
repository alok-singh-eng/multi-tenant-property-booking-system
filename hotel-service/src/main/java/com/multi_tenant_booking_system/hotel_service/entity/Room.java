package com.multi_tenant_booking_system.hotel_service.entity;

import java.math.BigDecimal;
import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "rooms")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Room {

  @Id
  private String id;
  private String hotelId;
  private String roomType;
  private int capacity;
  private BigDecimal basePrice;
  private Instant createdAt;
  private Instant updatedAt;
}
