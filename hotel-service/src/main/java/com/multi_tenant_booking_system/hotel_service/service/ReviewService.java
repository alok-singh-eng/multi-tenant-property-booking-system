package com.multi_tenant_booking_system.hotel_service.service;

import java.util.List;

import com.multi_tenant_booking_system.hotel_service.dto.request.CreateReviewRequest;
import com.multi_tenant_booking_system.hotel_service.dto.response.ReviewResponse;

public interface ReviewService {

  List<ReviewResponse> listReviews(String hotelId);

  ReviewResponse createReview(String hotelId, String authorEmail, CreateReviewRequest request);

  void deleteReview(String hotelId, String reviewId);
}
