package com.multi_tenant_booking_system.hotel_service.dto.request;

import com.multi_tenant_booking_system.hotel_service.dto.ImageStorageCategory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PresignImageUploadRequest {

  /** {@link ImageStorageCategory#HOTEL_IMAGE} or {@link ImageStorageCategory#ROOM_IMAGE} — determines GCS path under the bucket. */
  @NotNull
  private ImageStorageCategory category;

  @NotBlank
  @Size(max = 200)
  private String contentType;

  /** Required when {@code category == ROOM_IMAGE}; must be omitted for hotel images. */
  private String roomId;
}
