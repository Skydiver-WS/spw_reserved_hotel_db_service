package ru.project.reserved.system.db.app.service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.project.reserved.system.db.app.service.dto.room.RoomRq;
import ru.project.reserved.system.db.app.service.dto.room.RoomRs;
import ru.project.reserved.system.db.app.service.service.RoomService;

@RestController
@RequestMapping("/api/v1/room")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    @GetMapping
    public ResponseEntity<RoomRs> findAllRooms() {
        return ResponseEntity.ok(roomService.findAllRooms());
    }

    @PostMapping("/booking")
    public ResponseEntity<RoomRs> bookingOperation(@RequestBody @Valid RoomRq roomRq) {
        return ResponseEntity.ok(roomService.reservedRoom(roomRq));
    }

    @PostMapping("/search")
    public ResponseEntity<RoomRs> findRooms(@RequestBody @Valid RoomRq roomRq) {
        return ResponseEntity.ok(roomService.findRoomsForParameters(roomRq));
    }

    @PostMapping
    public ResponseEntity<RoomRs> createRoom(@RequestBody @Valid RoomRq roomRq) {
        return ResponseEntity.ok(roomService.createRoom(roomRq));
    }

    @PutMapping
    public ResponseEntity<RoomRs> updateRoom(@RequestBody @Valid RoomRq roomRq) {
        return ResponseEntity.ok(roomService.updateRoom(roomRq));
    }

    @DeleteMapping
    public ResponseEntity<RoomRs> deleteRoom(@RequestBody @Valid RoomRq roomRq) {
        return ResponseEntity.ok(roomService.removeRoom(roomRq));
    }
}
