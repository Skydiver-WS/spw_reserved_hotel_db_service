package ru.project.reserved.system.db.app.service.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.project.reserved.system.db.app.service.dto.room.RoomRequest;
import ru.project.reserved.system.db.app.service.dto.room.RoomResponse;
import ru.project.reserved.system.db.app.service.entity.Booking;
import ru.project.reserved.system.db.app.service.entity.Room;
import ru.project.reserved.system.db.app.service.exception.BookingException;
import ru.project.reserved.system.db.app.service.mapper.RoomMapper;
import ru.project.reserved.system.db.app.service.repository.BookingRepository;
import ru.project.reserved.system.db.app.service.repository.RoomRepository;
import ru.project.reserved.system.db.app.service.service.BookingService;
import ru.project.reserved.system.db.app.service.service.RoomSearchService;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final RoomSearchService search;
    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;
    private final BookingRepository bookingRepository;
    private final ObjectMapper objectMapper;

    @Override
    @SneakyThrows
    public RoomResponse createBookingRoom(RoomRequest roomRequest) {
        List<Room> roomResponses = search.searchRoomByParameterForReserved(roomRequest);
        if (roomResponses.isEmpty()) {
            throw new BookingException("Rooms by request not found. \n" +
                    "Request: " + objectMapper.writeValueAsString(roomRequest.getRoomBooking()));
        }
        Random random = new Random();
        Room room = roomResponses.size() == 1 ? roomResponses.getFirst()
                : roomResponses.get(random.nextInt(roomResponses.size()));
        Booking booking = new Booking();
        booking.setRoom(room);
        booking.setStartReserved(roomRequest.getRoomBooking().getStartReserved());
        booking.setEndReserved(roomRequest.getRoomBooking().getEndReserved());
        room.setBookings(List.of(booking));
        Room roomReserved = roomRepository.save(room);
        return roomMapper.roomToRoomResponse(roomReserved);
    }

    @Override
    public RoomResponse updateBookingRoom(RoomRequest roomRequest) {
        Optional<Booking> bookingOptional = bookingRepository.findById(roomRequest.getRoomBooking().getBookingId());
        if (bookingOptional.isEmpty()) {
            throw new BookingException("Booking with id " + roomRequest.getRoomBooking().getBookingId() + " not found.");
        }
        deleteBookingRoom(roomRequest);
        try {
            return createBookingRoom(roomRequest);
        } catch (BookingException e) {
            bookingRepository.save(bookingOptional.get());
            return RoomResponse.builder()
                    .bookingId(roomRequest.getRoomBooking().getBookingId())
                    .errorMessage(e.getMessage())
                    .build();
        }
    }

    @Override
    public RoomResponse deleteBookingRoom(RoomRequest roomRequest) {
        bookingRepository.deleteBookingById(roomRequest.getRoomBooking().getBookingId());

        if (bookingRepository.existsBookingById(roomRequest.getRoomBooking().getBookingId())) {
            throw new BookingException("Booking not remove");
        }
        return RoomResponse.builder()
                .bookingId(roomRequest.getRoomBooking().getBookingId())
                .description("Booking remove successfully")
                .build();
    }


}
