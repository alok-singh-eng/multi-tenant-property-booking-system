package com.multi_tenant_booking_system.hotel_service.service.impl;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import com.multi_tenant_booking_system.hotel_service.dto.request.CreateReviewRequest;
import com.multi_tenant_booking_system.hotel_service.dto.response.ReviewResponse;
import com.multi_tenant_booking_system.hotel_service.entity.Review;
import com.multi_tenant_booking_system.hotel_service.exception.ResourceNotFoundException;
import com.multi_tenant_booking_system.hotel_service.repository.HotelRepository;
import com.multi_tenant_booking_system.hotel_service.repository.ReviewRepository;
import com.multi_tenant_booking_system.hotel_service.service.ReviewService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

  private final HotelRepository hotelRepository;
  private final ReviewRepository reviewRepository;

  @Override
  public List<ReviewResponse> listReviews(String hotelId) {
    requireHotel(hotelId);
    return reviewRepository.findByHotelIdOrderByCreatedAtDesc(hotelId).stream().map(this::toResponse).toList();
  }

  @Override
  public ReviewResponse createReview(String hotelId, String authorEmail, CreateReviewRequest request) {
    requireHotel(hotelId);
    Review review = Review.builder()
        .hotelId(hotelId)
        .authorEmail(authorEmail)
        .rating(request.getRating())
        .comment(request.getComment() != null ? request.getComment() : "")
        .createdAt(Instant.now())
        .build();
    return toResponse(reviewRepository.save(review));
  }

  @Override
  public void deleteReview(String hotelId, String reviewId) {
    Review review = reviewRepository.findById(reviewId)
        .filter(r -> r.getHotelId().equals(hotelId))
        .orElseThrow(() -> new ResourceNotFoundException("Review not found: " + reviewId));
    reviewRepository.delete(review);
  }

  private void requireHotel(String hotelId) {
    if (!hotelRepository.existsById(hotelId)) {
      throw new ResourceNotFoundException("Hotel not found: " + hotelId);
    }
  }

  private ReviewResponse toResponse(Review r) {
    return ReviewResponse.builder()
        .id(r.getId())
        .hotelId(r.getHotelId())
        .authorEmail(r.getAuthorEmail())
        .rating(r.getRating())
        .comment(r.getComment())
        .createdAt(r.getCreatedAt())
        .build();
  }
}
