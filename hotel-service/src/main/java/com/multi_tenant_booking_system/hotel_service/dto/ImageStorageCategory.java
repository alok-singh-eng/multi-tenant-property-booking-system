package com.multi_tenant_booking_system.hotel_service.dto;

/** Where the object lives under the GCS bucket (separate folder trees). */
public enum ImageStorageCategory {
  /** Property-level gallery: {@code hotel-image/{hotelId}/...} */
  HOTEL_IMAGE,
  /** Room gallery: {@code hotel-room-image/{hotelId}/{roomId}/...} */
  ROOM_IMAGE
}
