package com.multi_tenant_booking_system.hotel_service.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.multi_tenant_booking_system.hotel_service.dto.request.CreateHotelRequest;
import com.multi_tenant_booking_system.hotel_service.dto.request.UpdateHotelRequest;
import com.multi_tenant_booking_system.hotel_service.dto.response.HotelResponse;
import com.multi_tenant_booking_system.hotel_service.service.HotelService;
import com.multi_tenant_booking_system.hotel_service.utility.ApiPath;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(ApiPath.HOTEL_BASE)
@RequiredArgsConstructor
public class HotelController {

  private final HotelService hotelService;

  @GetMapping
  public ResponseEntity<Page<HotelResponse>> listHotels(
      @PageableDefault(size = 20) Pageable pageable) {
    return ResponseEntity.ok(hotelService.listHotels(pageable));
  }

  @GetMapping("/{hotelId}")
  public ResponseEntity<HotelResponse> getHotel(@PathVariable String hotelId) {
    return ResponseEntity.ok(hotelService.getHotel(hotelId));
  }

  @PostMapping
  public ResponseEntity<HotelResponse> createHotel(@Valid @RequestBody CreateHotelRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(hotelService.createHotel(request));
  }

  @PutMapping("/{hotelId}")
  public ResponseEntity<HotelResponse> updateHotel(@PathVariable String hotelId,
      @Valid @RequestBody UpdateHotelRequest request) {
    return ResponseEntity.ok(hotelService.updateHotel(hotelId, request));
  }

  @DeleteMapping("/{hotelId}")
  public ResponseEntity<Void> deleteHotel(@PathVariable String hotelId) {
    hotelService.deleteHotel(hotelId);
    return ResponseEntity.noContent().build();
  }
}
