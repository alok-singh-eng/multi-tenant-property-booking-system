package com.multi_tenant_booking_system.hotel_service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = "app.storage.gcs")
public class GcsStorageProperties {

  private boolean enabled;
  /** GCP project id (optional if GOOGLE_CLOUD_PROJECT is set or inferrable from ADC). */
  private String projectId;
  /** Main bucket, e.g. {@code hotel-service-bucket}. */
  private String bucket;
  /**
   * HTTPS base used to build public image URLs returned to clients (no trailing slash). Example:
   * {@code https://storage.googleapis.com/hotel-service-bucket}
   */
  private String publicUrlBase;
  /** Top-level folder inside the bucket for hotel (property) images. */
  private String hotelImagePrefix = "hotel-image";
  /** Top-level folder inside the bucket for room-specific images. */
  private String roomImagePrefix = "hotel-room-image";
}
