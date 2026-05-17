package com.multi_tenant_booking_system.hotel_service.service.impl;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import com.multi_tenant_booking_system.hotel_service.dto.request.CreateRoomRequest;
import com.multi_tenant_booking_system.hotel_service.dto.request.UpdateRoomRequest;
import com.multi_tenant_booking_system.hotel_service.dto.response.RoomResponse;
import com.multi_tenant_booking_system.hotel_service.entity.Room;
import com.multi_tenant_booking_system.hotel_service.exception.ResourceNotFoundException;
import com.multi_tenant_booking_system.hotel_service.repository.HotelRepository;
import com.multi_tenant_booking_system.hotel_service.repository.RoomRepository;
import com.multi_tenant_booking_system.hotel_service.service.RoomService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

  private final HotelRepository hotelRepository;
  private final RoomRepository roomRepository;

  @Override
  public List<RoomResponse> listRooms(String hotelId) {
    requireHotel(hotelId);
    return roomRepository.findByHotelIdOrderByCreatedAtAsc(hotelId).stream().map(this::toResponse).toList();
  }

  @Override
  public RoomResponse getRoom(String hotelId, String roomId) {
    return toResponse(roomById(hotelId, roomId));
  }

  @Override
  public RoomResponse createRoom(String hotelId, CreateRoomRequest request) {
    requireHotel(hotelId);
    Instant now = Instant.now();
    Room room = Room.builder()
        .hotelId(hotelId)
        .roomType(request.getRoomType())
        .capacity(request.getCapacity())
        .basePrice(request.getBasePrice())
        .createdAt(now)
        .updatedAt(now)
        .build();
    return toResponse(roomRepository.save(room));
  }

  @Override
  public RoomResponse updateRoom(String hotelId, String roomId, UpdateRoomRequest request) {
    Room room = roomById(hotelId, roomId);
    if (request.getRoomType() != null) {
      room.setRoomType(request.getRoomType());
    }
    if (request.getCapacity() != null) {
      room.setCapacity(request.getCapacity());
    }
    if (request.getBasePrice() != null) {
      room.setBasePrice(request.getBasePrice());
    }
    room.setUpdatedAt(Instant.now());
    return toResponse(roomRepository.save(room));
  }

  @Override
  public void deleteRoom(String hotelId, String roomId) {
    roomRepository.delete(roomById(hotelId, roomId));
  }

  private void requireHotel(String hotelId) {
    if (!hotelRepository.existsById(hotelId)) {
      throw new ResourceNotFoundException("Hotel not found: " + hotelId);
    }
  }

  private Room roomById(String hotelId, String roomId) {
    return roomRepository.findByIdAndHotelId(roomId, hotelId)
        .orElseThrow(() -> new ResourceNotFoundException("Room not found: " + roomId));
  }

  private RoomResponse toResponse(Room r) {
    return RoomResponse.builder()
        .id(r.getId())
        .hotelId(r.getHotelId())
        .roomType(r.getRoomType())
        .capacity(r.getCapacity())
        .basePrice(r.getBasePrice())
        .createdAt(r.getCreatedAt())
        .updatedAt(r.getUpdatedAt())
        .build();
  }
}
