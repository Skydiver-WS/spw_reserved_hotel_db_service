package ru.project.reserved.system.db.app.service.service;

import ru.project.reserved.system.db.app.service.dto.room.RoomRq;
import ru.project.reserved.system.db.app.service.dto.room.RoomRs;

public interface BookingService {

    RoomRs createBookingRoom(RoomRq roomRq);
    RoomRs updateBookingRoom(RoomRq roomRq);
    RoomRs deleteBookingRoom(RoomRq roomRq);
}
