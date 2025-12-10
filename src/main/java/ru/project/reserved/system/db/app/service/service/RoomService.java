package ru.project.reserved.system.db.app.service.service;

import ru.project.reserved.system.db.app.service.dto.booking.BookingRs;
import ru.project.reserved.system.db.app.service.dto.room.RoomRq;
import ru.project.reserved.system.db.app.service.dto.room.RoomRs;

public interface RoomService {
    RoomRs findAllRooms();
    RoomRs createRoom(RoomRq room);
    RoomRs findRoomsForParameters(RoomRq request);
    RoomRs updateRoom(RoomRq room);
    RoomRs removeRoom(RoomRq roomRq);

    BookingRs reservedRoom(RoomRq roomRq);
}
