package com.multi_tenant_booking_system.hotel_service.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

@Configuration
public class GcsStorageConfiguration {

  @Bean
  @ConditionalOnProperty(prefix = "app.storage.gcs", name = "enabled", havingValue = "true")
  Storage gcsStorage(GcsStorageProperties props) {
    StorageOptions.Builder builder = StorageOptions.newBuilder();
    if (StringUtils.hasText(props.getProjectId())) {
      builder.setProjectId(props.getProjectId().trim());
    }
    return builder.build().getService();
  }
}
