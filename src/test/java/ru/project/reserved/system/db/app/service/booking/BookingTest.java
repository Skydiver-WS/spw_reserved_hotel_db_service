package ru.project.reserved.system.db.app.service.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.project.reserved.system.db.app.service.AbstractTest;
import ru.project.reserved.system.db.app.service.dto.booking.BookingRs;
import ru.project.reserved.system.db.app.service.dto.room.RoomRq;
import ru.project.reserved.system.db.app.service.dto.room.RoomRs;
import ru.project.reserved.system.db.app.service.dto.type.BookingOperationType;
import ru.project.reserved.system.db.app.service.entity.Booking;
import ru.project.reserved.system.db.app.service.entity.Room;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.UUID;

public class BookingTest extends AbstractTest {

    @Test
    public void createBooking() {
        RoomRq roomRq = RoomRq.builder()
                .roomBooking(RoomRq.RoomBooking.builder()
                        .startReserved(new Date())
                        .endReserved(new Date(LocalDateTime.now()
                                .plusDays(15)
                                .atZone(ZoneId.systemDefault()) // Учитываем часовой пояс
                                .toInstant()
                                .toEpochMilli()))
                        .hotelId(1L)
                        .operationType(BookingOperationType.CREATE)
                        .build())
                .build();
        BookingRs response = roomService.reservedRoom(roomRq);
        Booking booking = bookingRepository.findById(response.getBookingId()).orElse(null);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(booking.getId(), response.getBookingId());
    }

    @Test
    public void updateBooking() {
        Booking booking = bookings.getFirst();
        Room room = booking.getRoom();
        Long hotelId = room.getHotel().getId();
        RoomRq roomRq = RoomRq.builder()
                .roomBooking(RoomRq.RoomBooking.builder()
                        .startReserved(new Date(LocalDateTime.now()
                                .plusDays(10)
                                .atZone(ZoneId.systemDefault()) // Учитываем часовой пояс
                                .toInstant()
                                .toEpochMilli()))
                        .endReserved(new Date(LocalDateTime.now()
                                .plusDays(15)
                                .atZone(ZoneId.systemDefault()) // Учитываем часовой пояс
                                .toInstant()
                                .toEpochMilli()))
                        .hotelId(hotelId)
                        .bookingId(booking.getId())
                        .operationType(BookingOperationType.UPDATE)
                        .build())
                .build();

        BookingRs response = roomService.reservedRoom(roomRq);
        Booking booking1 = bookingRepository.findById(response.getBookingId()).orElse(null);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(booking1.getId(), response.getBookingId());
    }

    @Test
    public void deleteBooking() {
        UUID id = bookings.getFirst().getId();
        RoomRq roomRq = RoomRq.builder()
                .roomBooking(RoomRq.RoomBooking.builder()
                        .operationType(BookingOperationType.DELETE)
                        .bookingId(id)
                        .build())
                .build();
        BookingRs response  = roomService.reservedRoom(roomRq);
        Assertions.assertEquals("Booking remove successfully", response.getDescription());
    }
}
