package com.multi_tenant_booking_system.hotel_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.multi_tenant_booking_system.hotel_service.dto.request.CreateRoomRequest;
import com.multi_tenant_booking_system.hotel_service.dto.request.UpdateRoomRequest;
import com.multi_tenant_booking_system.hotel_service.dto.response.RoomResponse;
import com.multi_tenant_booking_system.hotel_service.service.RoomService;
import com.multi_tenant_booking_system.hotel_service.utility.ApiPath;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(ApiPath.HOTEL_BASE + "/{hotelId}/rooms")
@RequiredArgsConstructor
public class RoomController {

  private final RoomService roomService;

  @GetMapping
  public ResponseEntity<List<RoomResponse>> listRooms(@PathVariable String hotelId) {
    return ResponseEntity.ok(roomService.listRooms(hotelId));
  }

  @GetMapping("/{roomId}")
  public ResponseEntity<RoomResponse> getRoom(@PathVariable String hotelId, @PathVariable String roomId) {
    return ResponseEntity.ok(roomService.getRoom(hotelId, roomId));
  }

  @PostMapping
  public ResponseEntity<RoomResponse> createRoom(@PathVariable String hotelId,
      @Valid @RequestBody CreateRoomRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(roomService.createRoom(hotelId, request));
  }

  @PutMapping("/{roomId}")
  public ResponseEntity<RoomResponse> updateRoom(@PathVariable String hotelId, @PathVariable String roomId,
      @Valid @RequestBody UpdateRoomRequest request) {
    return ResponseEntity.ok(roomService.updateRoom(hotelId, roomId, request));
  }

  @DeleteMapping("/{roomId}")
  public ResponseEntity<Void> deleteRoom(@PathVariable String hotelId, @PathVariable String roomId) {
    roomService.deleteRoom(hotelId, roomId);
    return ResponseEntity.noContent().build();
  }
}
