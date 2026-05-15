package com.multi_tenant_booking_system.user_service.service;

import com.multi_tenant_booking_system.user_service.dto.request.CreateAdminRequest;

public interface AdminService {

  void createAdmin(CreateAdminRequest request);
}
