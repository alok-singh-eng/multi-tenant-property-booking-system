package com.multi_tenant_booking_system.hotel_service.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateHotelRequest {

  @Size(max = 200)
  private String name;

  @Size(max = 500)
  private String location;

  @Size(max = 5000)
  private String description;
}
