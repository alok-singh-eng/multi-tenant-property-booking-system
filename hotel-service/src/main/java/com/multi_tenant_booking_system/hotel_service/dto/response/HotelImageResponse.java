package com.multi_tenant_booking_system.hotel_service.dto.response;

import java.time.Instant;

import com.multi_tenant_booking_system.hotel_service.dto.ImageStorageCategory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HotelImageResponse {
  private String id;
  private String hotelId;
  private String roomId;
  private ImageStorageCategory storageCategory;
  private String imageUrl;
  private String objectKey;
  private Instant createdAt;
}
