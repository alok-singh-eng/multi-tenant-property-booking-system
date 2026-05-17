package com.multi_tenant_booking_system.hotel_service.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateReviewRequest {

  @NotNull
  @DecimalMin("1.0")
  @DecimalMax("5.0")
  private BigDecimal rating;

  @Size(max = 2000)
  private String comment;
}
