package ru.project.reserved.system.db.app.service.service;

import ru.project.reserved.system.db.app.service.dto.room.RoomRequest;
import ru.project.reserved.system.db.app.service.dto.room.RoomResponse;

import java.util.List;

public interface RoomService {
    List<RoomResponse> findAllRooms();
    RoomResponse createRoom(RoomRequest room);
    List<RoomResponse> findRoomsForParameters(RoomRequest request);
    RoomResponse updateRoom(RoomRequest room);
    RoomResponse removeRoom(Long hotelId, Long roomId);

    RoomResponse reservedRoom(RoomRequest roomRequest);
}
