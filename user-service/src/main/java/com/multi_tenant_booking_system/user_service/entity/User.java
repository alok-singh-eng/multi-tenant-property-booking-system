package com.multi_tenant_booking_system.user_service.entity;

import com.multi_tenant_booking_system.user_service.dto.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = User.USER_TABLE_NAME)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
 public static final String  USER_TABLE_NAME = "users";

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private String email;
    private String password;
    @Enumerated(EnumType.STRING)
    private Role role;
}
