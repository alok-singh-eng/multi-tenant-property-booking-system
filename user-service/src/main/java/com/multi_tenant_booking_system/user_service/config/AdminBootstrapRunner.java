package com.multi_tenant_booking_system.user_service.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.multi_tenant_booking_system.user_service.dto.Role;
import com.multi_tenant_booking_system.user_service.entity.User;
import com.multi_tenant_booking_system.user_service.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Order(100)
@RequiredArgsConstructor
@Slf4j
public class AdminBootstrapRunner implements ApplicationRunner {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Value("${app.bootstrap.admin.enabled:true}")
  private boolean enabled;

  @Value("${app.bootstrap.admin.name:System Administrator}")
  private String adminName;

  @Value("${app.bootstrap.admin.email:admin@localhost}")
  private String adminEmail;

  @Value("${app.bootstrap.admin.password:ChangeMe_ChangeMe_10}")
  private String adminPassword;

  @Override
  public void run(ApplicationArguments args) {
    if (!enabled) {
      log.info("Admin bootstrap skipped (app.bootstrap.admin.enabled=false).");
      return;
    }

    if (userRepository.countByRole(Role.ADMIN) > 0) {
      log.debug("At least one ADMIN exists; bootstrap skipped.");
      return;
    }

    userRepository.findByEmail(adminEmail).ifPresentOrElse(user -> {
      user.setName(adminName);
      user.setRole(Role.ADMIN);
      user.setPassword(passwordEncoder.encode(adminPassword));
      userRepository.save(user);
      log.warn(
          "No ADMIN users were found. Promoted existing account {} to ADMIN. Change the password immediately.",
          adminEmail);
    }, () -> {
      User admin = User.builder()
          .name(adminName)
          .email(adminEmail)
          .password(passwordEncoder.encode(adminPassword))
          .role(Role.ADMIN)
          .build();
      userRepository.save(admin);
      log.warn(
          "No ADMIN users were found. Created bootstrap ADMIN {}. Change the password immediately.",
          adminEmail);
    });
  }
}
