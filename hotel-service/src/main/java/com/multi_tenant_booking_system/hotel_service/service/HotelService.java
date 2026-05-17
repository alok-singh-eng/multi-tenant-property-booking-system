package com.multi_tenant_booking_system.hotel_service.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.multi_tenant_booking_system.hotel_service.dto.request.CreateHotelRequest;
import com.multi_tenant_booking_system.hotel_service.dto.request.UpdateHotelRequest;
import com.multi_tenant_booking_system.hotel_service.dto.response.HotelResponse;

public interface HotelService {

  Page<HotelResponse> listHotels(Pageable pageable);

  HotelResponse getHotel(String id);

  HotelResponse createHotel(CreateHotelRequest request);

  HotelResponse updateHotel(String id, UpdateHotelRequest request);

  void deleteHotel(String id);
}
