package ru.project.reserved.system.db.app.service.service;

import ru.project.reserved.system.db.app.service.dto.RoomRequest;
import ru.project.reserved.system.db.app.service.dto.RoomResponse;

import java.util.List;

public interface RoomService {
    List<RoomResponse> findAllRooms();
    RoomResponse createRoom(RoomRequest room);
    List<RoomResponse> findRoomsForParameters(RoomRequest room);
    RoomResponse updateRoom(RoomRequest room);
    void removeRoom(Integer id);
}
