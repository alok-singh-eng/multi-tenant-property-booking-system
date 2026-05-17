package com.multi_tenant_booking_system.hotel_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateHotelRequest {

  @NotBlank(message = "Name is required")
  @Size(max = 200)
  private String name;

  @NotBlank(message = "Location is required")
  @Size(max = 500)
  private String location;

  @Size(max = 5000)
  private String description;
}
