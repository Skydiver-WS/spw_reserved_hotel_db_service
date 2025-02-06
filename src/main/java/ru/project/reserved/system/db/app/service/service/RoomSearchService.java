package ru.project.reserved.system.db.app.service.service;

import ru.project.reserved.system.db.app.service.dto.room.RoomRequest;
import ru.project.reserved.system.db.app.service.dto.room.RoomResponse;
import ru.project.reserved.system.db.app.service.entity.Room;

import java.util.List;

public interface RoomSearchService {
    List<RoomResponse> searchRoomByParameter(RoomRequest roomRequest);
    List<Room> searchRoomByParameterForReserved(RoomRequest roomRequest);
}
