package ru.project.reserved.system.db.app.service.service;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;
import ru.project.reserved.system.db.app.service.dto.room.RoomRequest;
import ru.project.reserved.system.db.app.service.dto.room.RoomResponse;

import java.util.List;

public interface RoomService {
    RoomResponse findAllRooms();
    RoomResponse createRoom(RoomRequest room);
    RoomResponse findRoomsForParameters(RoomRequest request);
    RoomResponse updateRoom(RoomRequest room);
    RoomResponse removeRoom(RoomRequest roomRequest);

    RoomResponse reservedRoom(RoomRequest roomRequest);
}
