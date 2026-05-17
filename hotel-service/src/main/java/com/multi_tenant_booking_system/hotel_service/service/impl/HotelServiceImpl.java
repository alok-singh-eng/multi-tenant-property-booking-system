package com.multi_tenant_booking_system.hotel_service.service.impl;

import java.time.Instant;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.multi_tenant_booking_system.hotel_service.dto.request.CreateHotelRequest;
import com.multi_tenant_booking_system.hotel_service.dto.request.UpdateHotelRequest;
import com.multi_tenant_booking_system.hotel_service.dto.response.HotelResponse;
import com.multi_tenant_booking_system.hotel_service.entity.Hotel;
import com.multi_tenant_booking_system.hotel_service.exception.ResourceNotFoundException;
import com.multi_tenant_booking_system.hotel_service.repository.HotelImageRepository;
import com.multi_tenant_booking_system.hotel_service.repository.HotelRepository;
import com.multi_tenant_booking_system.hotel_service.repository.ReviewRepository;
import com.multi_tenant_booking_system.hotel_service.repository.RoomRepository;
import com.multi_tenant_booking_system.hotel_service.service.HotelService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

  private final HotelRepository hotelRepository;
  private final RoomRepository roomRepository;
  private final ReviewRepository reviewRepository;
  private final HotelImageRepository hotelImageRepository;

  @Override
  public Page<HotelResponse> listHotels(Pageable pageable) {
    return hotelRepository.findAllByOrderByCreatedAtDesc(pageable).map(this::toResponse);
  }

  @Override
  public HotelResponse getHotel(String id) {
    return toResponse(hotelById(id));
  }

  @Override
  public HotelResponse createHotel(CreateHotelRequest request) {
    Instant now = Instant.now();
    Hotel hotel = Hotel.builder()
        .name(request.getName())
        .location(request.getLocation())
        .description(request.getDescription() != null ? request.getDescription() : "")
        .createdAt(now)
        .updatedAt(now)
        .build();
    return toResponse(hotelRepository.save(hotel));
  }

  @Override
  public HotelResponse updateHotel(String id, UpdateHotelRequest request) {
    Hotel hotel = hotelById(id);
    if (request.getName() != null) {
      hotel.setName(request.getName());
    }
    if (request.getLocation() != null) {
      hotel.setLocation(request.getLocation());
    }
    if (request.getDescription() != null) {
      hotel.setDescription(request.getDescription());
    }
    hotel.setUpdatedAt(Instant.now());
    return toResponse(hotelRepository.save(hotel));
  }

  @Override
  public void deleteHotel(String id) {
    hotelById(id);
    roomRepository.deleteByHotelId(id);
    reviewRepository.deleteByHotelId(id);
    hotelImageRepository.deleteByHotelId(id);
    hotelRepository.deleteById(id);
  }

  private Hotel hotelById(String id) {
    return hotelRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Hotel not found: " + id));
  }

  private HotelResponse toResponse(Hotel h) {
    return HotelResponse.builder()
        .id(h.getId())
        .name(h.getName())
        .location(h.getLocation())
        .description(h.getDescription())
        .createdAt(h.getCreatedAt())
        .updatedAt(h.getUpdatedAt())
        .build();
  }
}
