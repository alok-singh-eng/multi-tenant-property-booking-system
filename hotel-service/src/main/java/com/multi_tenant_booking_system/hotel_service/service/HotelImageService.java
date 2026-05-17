package com.multi_tenant_booking_system.hotel_service.service;

import java.util.List;

import com.multi_tenant_booking_system.hotel_service.dto.request.PresignImageUploadRequest;
import com.multi_tenant_booking_system.hotel_service.dto.request.RegisterHotelImageRequest;
import com.multi_tenant_booking_system.hotel_service.dto.response.HotelImageResponse;
import com.multi_tenant_booking_system.hotel_service.dto.response.PresignedUploadResponse;

public interface HotelImageService {

  List<HotelImageResponse> listImages(String hotelId);

  PresignedUploadResponse presignUpload(String hotelId, PresignImageUploadRequest request);

  HotelImageResponse registerImage(String hotelId, RegisterHotelImageRequest request);

  void deleteImage(String hotelId, String imageId);
}
