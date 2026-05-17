package com.multi_tenant_booking_system.hotel_service.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.multi_tenant_booking_system.hotel_service.entity.Review;

@Repository
public interface ReviewRepository extends MongoRepository<Review, String> {

  List<Review> findByHotelIdOrderByCreatedAtDesc(String hotelId);

  void deleteByHotelId(String hotelId);
}
