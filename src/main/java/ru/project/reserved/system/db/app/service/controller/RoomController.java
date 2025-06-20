package ru.project.reserved.system.db.app.service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.project.reserved.system.db.app.service.dto.room.RoomRequest;
import ru.project.reserved.system.db.app.service.dto.room.RoomResponse;
import ru.project.reserved.system.db.app.service.service.RoomService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/room")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    @GetMapping
    public ResponseEntity<List<RoomResponse>> findAllRooms() {
        return ResponseEntity.ok(roomService.findAllRooms());
    }

    @PutMapping("/booking")
    public ResponseEntity<RoomResponse> bookingOperation(@RequestBody @Valid RoomRequest roomRequest) {
        return ResponseEntity.ok(roomService.reservedRoom(roomRequest));
    }

    @PostMapping("/search")
    public ResponseEntity<List<RoomResponse>> findRooms(@RequestBody @Valid RoomRequest roomRequest) {
        return ResponseEntity.ok(roomService.findRoomsForParameters(roomRequest));
    }

    @PostMapping
    public ResponseEntity<RoomResponse> createRoom(@RequestBody @Valid RoomRequest roomRequest) {
        return ResponseEntity.ok(roomService.createRoom(roomRequest));
    }

    @PutMapping
    public ResponseEntity<RoomResponse> updateRoom(@RequestBody @Valid RoomRequest roomRequest) {
        return ResponseEntity.ok(roomService.updateRoom(roomRequest));
    }

    @DeleteMapping
    public ResponseEntity<RoomResponse> deleteRoom(@RequestParam Long hotelId, @RequestParam Long roomId) {
        return ResponseEntity.ok(roomService.removeRoom(hotelId, roomId));
    }
}
