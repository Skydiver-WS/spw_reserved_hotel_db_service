package ru.project.reserved.system.db.app.service.service;

import org.springframework.data.domain.Pageable;
import ru.project.reserved.system.db.app.service.dto.booking.BookingRs;
import ru.project.reserved.system.db.app.service.dto.room.RoomRq;
import ru.project.reserved.system.db.app.service.dto.room.RoomRs;

public interface RoomService {
    RoomRs findAllRooms(Pageable pageable);
    RoomRs createRoom(RoomRq room);
    RoomRs findRoomsForParameters(RoomRq request, Pageable pageable);
    RoomRs updateRoom(RoomRq room);
    RoomRs removeRoom(RoomRq roomRq);

    BookingRs reservedRoom(RoomRq roomRq);
}
