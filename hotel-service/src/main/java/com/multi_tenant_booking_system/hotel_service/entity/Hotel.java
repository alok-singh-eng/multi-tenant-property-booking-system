package com.multi_tenant_booking_system.hotel_service.entity;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "hotels")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Hotel {

  @Id
  private String id;
  private String name;
  private String location;
  private String description;
  private Instant createdAt;
  private Instant updatedAt;
}
