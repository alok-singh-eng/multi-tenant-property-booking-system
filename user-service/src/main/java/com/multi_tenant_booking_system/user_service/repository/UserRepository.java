package com.multi_tenant_booking_system.user_service.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.multi_tenant_booking_system.user_service.dto.Role;
import com.multi_tenant_booking_system.user_service.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
  Optional<User> findByEmail(String email);

  boolean existsByEmail(String email);

  long countByRole(Role role);
}
