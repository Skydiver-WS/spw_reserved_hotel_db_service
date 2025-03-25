package ru.project.reserved.system.db.app.service.service;

import ru.project.reserved.system.db.app.service.dto.room.RoomRequest;
import ru.project.reserved.system.db.app.service.dto.room.RoomResponse;

public interface BookingService {

    RoomResponse createBookingRoom(RoomRequest roomRequest);
    RoomResponse updateBookingRoom(RoomRequest roomRequest);
    RoomResponse deleteBookingRoom(RoomRequest roomRequest);
}
