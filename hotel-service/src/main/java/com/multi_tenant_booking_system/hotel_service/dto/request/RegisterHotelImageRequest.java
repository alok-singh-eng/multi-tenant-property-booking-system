package com.multi_tenant_booking_system.hotel_service.dto.request;

import com.multi_tenant_booking_system.hotel_service.dto.ImageStorageCategory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegisterHotelImageRequest {

  @NotNull
  private ImageStorageCategory category;

  /** Must match the key returned from the presign step. */
  @NotBlank
  @Size(max = 1024)
  private String objectKey;

  /** Required when {@code category == ROOM_IMAGE}. */
  private String roomId;
}
