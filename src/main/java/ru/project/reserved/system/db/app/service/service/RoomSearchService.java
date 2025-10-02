package ru.project.reserved.system.db.app.service.service;

import ru.project.reserved.system.db.app.service.dto.room.RoomRq;
import ru.project.reserved.system.db.app.service.entity.Room;

import java.util.List;

public interface RoomSearchService {
    List<Room> searchRoomByParameter(RoomRq roomRq);
    List<Room> searchRoomByParameterForReserved(RoomRq roomRq);
}
