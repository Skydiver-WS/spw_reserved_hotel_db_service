package ru.project.reserved.system.db.app.service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.project.reserved.system.db.app.service.aop.Metric;
import ru.project.reserved.system.db.app.service.dto.booking.BookingRs;
import ru.project.reserved.system.db.app.service.dto.room.RoomRq;
import ru.project.reserved.system.db.app.service.dto.room.RoomRs;
import ru.project.reserved.system.db.app.service.dto.type.MetricType;
import ru.project.reserved.system.db.app.service.service.RoomService;

@RestController
@RequestMapping("/api/v1/room")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    @GetMapping
    @Metric(type = MetricType.ALL_ROOMS, description = "Get all rooms")
    public ResponseEntity<RoomRs> findAllRooms() {
        return ResponseEntity.ok(roomService.findAllRooms());
    }

    @PostMapping("/booking")
    public ResponseEntity<BookingRs> bookingOperation(@RequestBody @Valid RoomRq roomRq) {
        return ResponseEntity.ok(roomService.reservedRoom(roomRq));
    }

    @PostMapping("/search")
    @Metric(type = MetricType.FIND_ROOMS, description = "Find rooms")
    public ResponseEntity<RoomRs> findRooms(@RequestBody @Valid RoomRq roomRq) {
        return ResponseEntity.ok(roomService.findRoomsForParameters(roomRq));
    }

    @PostMapping
    @Metric(type = MetricType.CREATE_ROOM, description = "Create room")
    public ResponseEntity<RoomRs> createRoom(@RequestBody @Valid RoomRq roomRq) {
        return ResponseEntity.ok(roomService.createRoom(roomRq));
    }

    @PutMapping
    @Metric(type = MetricType.UPDATE_ROOM, description = "Update room")
    public ResponseEntity<RoomRs> updateRoom(@RequestBody @Valid RoomRq roomRq) {
        return ResponseEntity.ok(roomService.updateRoom(roomRq));
    }

    @DeleteMapping
    @Metric(type = MetricType.DELETE_ROOM, description = "Delete room")
    public ResponseEntity<RoomRs> deleteRoom(@RequestBody @Valid RoomRq roomRq) {
        return ResponseEntity.ok(roomService.removeRoom(roomRq));
    }
}
