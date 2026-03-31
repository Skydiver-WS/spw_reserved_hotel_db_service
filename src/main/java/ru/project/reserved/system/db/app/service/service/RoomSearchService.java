package ru.project.reserved.system.db.app.service.service;

import org.springframework.data.domain.Pageable;
import ru.project.reserved.system.db.app.service.dto.room.RoomRq;
import ru.project.reserved.system.db.app.service.entity.Room;

import java.util.List;

public interface RoomSearchService {
    List<Room> searchRoomByParameter(RoomRq roomRq, Pageable pageable);
    List<Room> searchRoomByParameterForReserved(RoomRq roomRq);
}
