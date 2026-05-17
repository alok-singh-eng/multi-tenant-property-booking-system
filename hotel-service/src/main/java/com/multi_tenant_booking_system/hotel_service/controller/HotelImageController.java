package com.multi_tenant_booking_system.hotel_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.multi_tenant_booking_system.hotel_service.dto.request.PresignImageUploadRequest;
import com.multi_tenant_booking_system.hotel_service.dto.request.RegisterHotelImageRequest;
import com.multi_tenant_booking_system.hotel_service.dto.response.HotelImageResponse;
import com.multi_tenant_booking_system.hotel_service.dto.response.PresignedUploadResponse;
import com.multi_tenant_booking_system.hotel_service.service.HotelImageService;
import com.multi_tenant_booking_system.hotel_service.utility.ApiPath;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(ApiPath.HOTEL_BASE + "/{hotelId}/images")
@RequiredArgsConstructor
public class HotelImageController {

  private final HotelImageService hotelImageService;

  @GetMapping
  public ResponseEntity<List<HotelImageResponse>> listImages(@PathVariable String hotelId) {
    return ResponseEntity.ok(hotelImageService.listImages(hotelId));
  }

  @PostMapping("/presign")
  public ResponseEntity<PresignedUploadResponse> presign(@PathVariable String hotelId,
      @Valid @RequestBody PresignImageUploadRequest request) {
    return ResponseEntity.ok(hotelImageService.presignUpload(hotelId, request));
  }

  @PostMapping
  public ResponseEntity<HotelImageResponse> registerImage(@PathVariable String hotelId,
      @Valid @RequestBody RegisterHotelImageRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(hotelImageService.registerImage(hotelId, request));
  }

  @DeleteMapping("/{imageId}")
  public ResponseEntity<Void> deleteImage(@PathVariable String hotelId, @PathVariable String imageId) {
    hotelImageService.deleteImage(hotelId, imageId);
    return ResponseEntity.noContent().build();
  }
}
