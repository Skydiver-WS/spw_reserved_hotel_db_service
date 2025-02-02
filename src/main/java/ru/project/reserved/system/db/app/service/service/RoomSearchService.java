package ru.project.reserved.system.db.app.service.service;

import ru.project.reserved.system.db.app.service.dto.room.RoomRequest;
import ru.project.reserved.system.db.app.service.dto.room.RoomResponse;

import java.util.List;

public interface RoomSearchService {
    List<RoomResponse> searchRoomByParameter(RoomRequest roomRequest);
}
