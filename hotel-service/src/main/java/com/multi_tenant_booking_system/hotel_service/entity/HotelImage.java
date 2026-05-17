package com.multi_tenant_booking_system.hotel_service.entity;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.multi_tenant_booking_system.hotel_service.dto.ImageStorageCategory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "hotel_images")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HotelImage {

  @Id
  private String id;
  private String hotelId;
  /** Set for {@link ImageStorageCategory#ROOM_IMAGE}; null for hotel-level images. */
  private String roomId;
  /** HOTEL_IMAGE vs ROOM_IMAGE — matches GCS folder ({@code hotel-image} vs {@code hotel-room-image}). */
  private ImageStorageCategory storageCategory;
  /** Canonical HTTPS URL clients use to render the image. */
  private String imageUrl;
  /** GCS object name (path inside the bucket). */
  private String objectKey;
  private Instant createdAt;
}
