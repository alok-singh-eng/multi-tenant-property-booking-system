package com.multi_tenant_booking_system.hotel_service.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateRoomRequest {

  @Size(max = 120)
  private String roomType;

  @Min(1)
  private Integer capacity;

  @DecimalMin("0.01")
  private BigDecimal basePrice;
}
