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

import com.multi_tenant_booking_system.hotel_service.dto.request.CreateReviewRequest;
import com.multi_tenant_booking_system.hotel_service.dto.response.ReviewResponse;
import com.multi_tenant_booking_system.hotel_service.security.SecurityUtils;
import com.multi_tenant_booking_system.hotel_service.service.ReviewService;
import com.multi_tenant_booking_system.hotel_service.utility.ApiPath;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(ApiPath.HOTEL_BASE + "/{hotelId}/reviews")
@RequiredArgsConstructor
public class ReviewController {

  private final ReviewService reviewService;

  @GetMapping
  public ResponseEntity<List<ReviewResponse>> listReviews(@PathVariable String hotelId) {
    return ResponseEntity.ok(reviewService.listReviews(hotelId));
  }

  @PostMapping
  public ResponseEntity<ReviewResponse> createReview(@PathVariable String hotelId,
      @Valid @RequestBody CreateReviewRequest request) {
    String email = SecurityUtils.requireAuthenticatedUser().email();
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(reviewService.createReview(hotelId, email, request));
  }

  @DeleteMapping("/{reviewId}")
  public ResponseEntity<Void> deleteReview(@PathVariable String hotelId, @PathVariable String reviewId) {
    reviewService.deleteReview(hotelId, reviewId);
    return ResponseEntity.noContent().build();
  }
}
