package com.multi_tenant_booking_system.hotel_service.service.impl;

import java.net.URL;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.HttpMethod;
import com.google.cloud.storage.Storage;
import com.multi_tenant_booking_system.hotel_service.config.GcsStorageProperties;
import com.multi_tenant_booking_system.hotel_service.dto.ImageStorageCategory;
import com.multi_tenant_booking_system.hotel_service.dto.request.PresignImageUploadRequest;
import com.multi_tenant_booking_system.hotel_service.dto.request.RegisterHotelImageRequest;
import com.multi_tenant_booking_system.hotel_service.dto.response.HotelImageResponse;
import com.multi_tenant_booking_system.hotel_service.dto.response.PresignedUploadResponse;
import com.multi_tenant_booking_system.hotel_service.entity.HotelImage;
import com.multi_tenant_booking_system.hotel_service.exception.HotelServiceException;
import com.multi_tenant_booking_system.hotel_service.exception.ResourceNotFoundException;
import com.multi_tenant_booking_system.hotel_service.repository.HotelImageRepository;
import com.multi_tenant_booking_system.hotel_service.repository.HotelRepository;
import com.multi_tenant_booking_system.hotel_service.repository.RoomRepository;
import com.multi_tenant_booking_system.hotel_service.service.HotelImageService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HotelImageServiceImpl implements HotelImageService {

  private static final int PRESIGN_TTL_SECONDS = 900;

  private final HotelRepository hotelRepository;
  private final RoomRepository roomRepository;
  private final HotelImageRepository hotelImageRepository;
  private final GcsStorageProperties gcsProperties;
  private final ObjectProvider<Storage> gcsStorage;

  @Override
  public List<HotelImageResponse> listImages(String hotelId) {
    requireHotel(hotelId);
    return hotelImageRepository.findByHotelIdOrderByCreatedAtAsc(hotelId).stream().map(this::toResponse).toList();
  }

  @Override
  public PresignedUploadResponse presignUpload(String hotelId, PresignImageUploadRequest request) {
    requireHotel(hotelId);
    if (!gcsProperties.isEnabled()) {
      throw new HotelServiceException(HttpStatus.BAD_REQUEST,
          "GCS signed uploads are disabled. Set app.storage.gcs.enabled=true and configure bucket/credentials. See docs/hotel-service/SIGNED_URL_SETUP.md");
    }
    Storage storage = gcsStorage.getIfAvailable();
    if (storage == null) {
      throw new HotelServiceException(HttpStatus.SERVICE_UNAVAILABLE,
          "GCS Storage bean is not available. Enable app.storage.gcs.enabled and verify Application Default Credentials.");
    }

    ImageStorageCategory category = request.getCategory();
    if (category == null) {
      throw new HotelServiceException(HttpStatus.BAD_REQUEST, "category is required (HOTEL_IMAGE or ROOM_IMAGE).");
    }

    String suffix = objectSuffixFromContentType(request.getContentType());

    final String objectKey;
    if (category == ImageStorageCategory.HOTEL_IMAGE) {
      if (StringUtils.hasText(request.getRoomId())) {
        throw new HotelServiceException(HttpStatus.BAD_REQUEST,
            "roomId must not be set for HOTEL_IMAGE uploads (use hotel-image/ path only).");
      }
      objectKey = gcsProperties.getHotelImagePrefix() + "/" + hotelId + "/" + UUID.randomUUID() + suffix;
    }
    else if (category == ImageStorageCategory.ROOM_IMAGE) {
      if (!StringUtils.hasText(request.getRoomId())) {
        throw new HotelServiceException(HttpStatus.BAD_REQUEST, "roomId is required for ROOM_IMAGE uploads.");
      }
      roomRepository.findByIdAndHotelId(request.getRoomId(), hotelId)
          .orElseThrow(() -> new ResourceNotFoundException("Room not found: " + request.getRoomId()));
      objectKey = gcsProperties.getRoomImagePrefix() + "/" + hotelId + "/" + request.getRoomId() + "/"
          + UUID.randomUUID() + suffix;
    }
    else {
      throw new HotelServiceException(HttpStatus.BAD_REQUEST, "Unsupported image category.");
    }

    BlobInfo blobInfo = BlobInfo.newBuilder(gcsProperties.getBucket(), objectKey)
        .setContentType(request.getContentType())
        .build();

    URL signedUrl = storage.signUrl(blobInfo, PRESIGN_TTL_SECONDS, TimeUnit.SECONDS,
        Storage.SignUrlOption.httpMethod(HttpMethod.PUT),
        Storage.SignUrlOption.withV4Signature(),
        Storage.SignUrlOption.withContentType());

    Map<String, String> headers = new LinkedHashMap<>();
    headers.put("Content-Type", request.getContentType());

    return PresignedUploadResponse.builder()
        .httpMethod("PUT")
        .uploadUrl(signedUrl.toString())
        .headers(headers)
        .objectKey(objectKey)
        .publicUrl(buildPublicUrl(objectKey))
        .expiresInSeconds(PRESIGN_TTL_SECONDS)
        .build();
  }

  @Override
  public HotelImageResponse registerImage(String hotelId, RegisterHotelImageRequest request) {
    requireHotel(hotelId);
    ImageStorageCategory category = request.getCategory();
    if (category == null) {
      throw new HotelServiceException(HttpStatus.BAD_REQUEST, "category is required (HOTEL_IMAGE or ROOM_IMAGE).");
    }

    String expectedPrefix = expectedKeyPrefix(hotelId, category, request.getRoomId());
    if (!request.getObjectKey().startsWith(expectedPrefix)) {
      throw new HotelServiceException(HttpStatus.BAD_REQUEST,
          "objectKey does not match expected prefix for category " + category + ": " + expectedPrefix);
    }

    final String resolvedRoomId;
    if (category == ImageStorageCategory.HOTEL_IMAGE) {
      if (StringUtils.hasText(request.getRoomId())) {
        throw new HotelServiceException(HttpStatus.BAD_REQUEST, "roomId must not be set when category is HOTEL_IMAGE.");
      }
      resolvedRoomId = null;
    }
    else {
      if (!StringUtils.hasText(request.getRoomId())) {
        throw new HotelServiceException(HttpStatus.BAD_REQUEST, "roomId is required when category is ROOM_IMAGE.");
      }
      resolvedRoomId = request.getRoomId().trim();
      roomRepository.findByIdAndHotelId(resolvedRoomId, hotelId)
          .orElseThrow(() -> new ResourceNotFoundException("Room not found: " + resolvedRoomId));
      if (!request.getObjectKey().startsWith(
          gcsProperties.getRoomImagePrefix() + "/" + hotelId + "/" + resolvedRoomId + "/")) {
        throw new HotelServiceException(HttpStatus.BAD_REQUEST,
            "objectKey must be under hotel-room-image/{hotelId}/{roomId}/ for ROOM_IMAGE.");
      }
    }

    String imageUrl = buildPublicUrl(request.getObjectKey());
    HotelImage img = HotelImage.builder()
        .hotelId(hotelId)
        .roomId(resolvedRoomId)
        .storageCategory(category)
        .objectKey(request.getObjectKey())
        .imageUrl(imageUrl)
        .createdAt(Instant.now())
        .build();
    return toResponse(hotelImageRepository.save(img));
  }

  @Override
  public void deleteImage(String hotelId, String imageId) {
    HotelImage img = hotelImageRepository.findById(imageId)
        .filter(i -> i.getHotelId().equals(hotelId))
        .orElseThrow(() -> new ResourceNotFoundException("Image not found: " + imageId));
    hotelImageRepository.delete(img);
  }

  private String expectedKeyPrefix(String hotelId, ImageStorageCategory category, String roomId) {
    if (category == ImageStorageCategory.HOTEL_IMAGE) {
      return gcsProperties.getHotelImagePrefix() + "/" + hotelId + "/";
    }
    if (category == ImageStorageCategory.ROOM_IMAGE) {
      if (!StringUtils.hasText(roomId)) {
        throw new HotelServiceException(HttpStatus.BAD_REQUEST, "roomId is required for ROOM_IMAGE registration.");
      }
      return gcsProperties.getRoomImagePrefix() + "/" + hotelId + "/" + roomId.trim() + "/";
    }
    throw new HotelServiceException(HttpStatus.BAD_REQUEST, "Unsupported image category.");
  }

  private void requireHotel(String hotelId) {
    if (!hotelRepository.existsById(hotelId)) {
      throw new ResourceNotFoundException("Hotel not found: " + hotelId);
    }
  }

  private String buildPublicUrl(String objectKey) {
    String base = gcsProperties.getPublicUrlBase();
    if (base == null || base.isBlank()) {
      throw new HotelServiceException(HttpStatus.BAD_REQUEST,
          "app.storage.gcs.public-url-base must be set to the HTTPS base used by clients to load images (e.g. https://storage.googleapis.com/hotel-service-bucket).");
    }
    String trimmed = base.endsWith("/") ? base.substring(0, base.length() - 1) : base;
    return trimmed + "/" + objectKey;
  }

  /**
   * Optional file suffix for the object name (GCS stores MIME on the blob; suffix helps browsing).
   */
  private static String objectSuffixFromContentType(String contentType) {
    if (contentType == null || contentType.isBlank()) {
      return "";
    }
    String lower = contentType.trim().toLowerCase();
    if (lower.startsWith("image/jpeg") || lower.startsWith("image/jpg")) {
      return ".jpg";
    }
    if (lower.startsWith("image/png")) {
      return ".png";
    }
    if (lower.startsWith("image/gif")) {
      return ".gif";
    }
    if (lower.startsWith("image/webp")) {
      return ".webp";
    }
    if (lower.startsWith("image/svg+xml")) {
      return ".svg";
    }
    return "";
  }

  private HotelImageResponse toResponse(HotelImage i) {
    return HotelImageResponse.builder()
        .id(i.getId())
        .hotelId(i.getHotelId())
        .roomId(i.getRoomId())
        .storageCategory(i.getStorageCategory())
        .imageUrl(i.getImageUrl())
        .objectKey(i.getObjectKey())
        .createdAt(i.getCreatedAt())
        .build();
  }
}
