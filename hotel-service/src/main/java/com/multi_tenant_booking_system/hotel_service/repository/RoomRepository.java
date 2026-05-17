package com.multi_tenant_booking_system.hotel_service.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.multi_tenant_booking_system.hotel_service.entity.Room;

@Repository
public interface RoomRepository extends MongoRepository<Room, String> {

  List<Room> findByHotelIdOrderByCreatedAtAsc(String hotelId);

  Optional<Room> findByIdAndHotelId(String id, String hotelId);

  void deleteByHotelId(String hotelId);
}
