package ru.project.reserved.system.db.app.service.service;

import ru.project.reserved.system.db.app.service.dto.booking.BookingRs;
import ru.project.reserved.system.db.app.service.dto.room.RoomRq;
import ru.project.reserved.system.db.app.service.dto.room.RoomRs;

public interface BookingService {

    BookingRs createBookingRoom(RoomRq roomRq);
    BookingRs updateBookingRoom(RoomRq roomRq);
    BookingRs deleteBookingRoom(RoomRq roomRq);
}
