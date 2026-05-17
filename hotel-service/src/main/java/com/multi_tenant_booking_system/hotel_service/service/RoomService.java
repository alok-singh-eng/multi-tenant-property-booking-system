package com.multi_tenant_booking_system.hotel_service.service;

import java.util.List;

import com.multi_tenant_booking_system.hotel_service.dto.request.CreateRoomRequest;
import com.multi_tenant_booking_system.hotel_service.dto.request.UpdateRoomRequest;
import com.multi_tenant_booking_system.hotel_service.dto.response.RoomResponse;

public interface RoomService {

  List<RoomResponse> listRooms(String hotelId);

  RoomResponse getRoom(String hotelId, String roomId);

  RoomResponse createRoom(String hotelId, CreateRoomRequest request);

  RoomResponse updateRoom(String hotelId, String roomId, UpdateRoomRequest request);

  void deleteRoom(String hotelId, String roomId);
}
