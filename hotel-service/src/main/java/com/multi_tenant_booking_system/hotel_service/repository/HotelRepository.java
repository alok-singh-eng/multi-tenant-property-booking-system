package com.multi_tenant_booking_system.hotel_service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.multi_tenant_booking_system.hotel_service.entity.Hotel;

@Repository
public interface HotelRepository extends MongoRepository<Hotel, String> {

  Page<Hotel> findAllByOrderByCreatedAtDesc(Pageable pageable);
}
