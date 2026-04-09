package ru.project.reserved.system.db.app.service.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.project.reserved.system.db.app.service.dto.booking.BookingRs;
import ru.project.reserved.system.db.app.service.dto.room.RoomRq;
import ru.project.reserved.system.db.app.service.entity.Booking;
import ru.project.reserved.system.db.app.service.entity.Room;
import ru.project.reserved.system.db.app.service.exception.BookingException;
import ru.project.reserved.system.db.app.service.mapper.BookingMapper;
import ru.project.reserved.system.db.app.service.repository.BookingRepository;
import ru.project.reserved.system.db.app.service.repository.RoomRepository;
import ru.project.reserved.system.db.app.service.service.BookingService;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import static ru.project.reserved.system.db.app.service.specification.RoomSearchSpecification.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final ObjectMapper objectMapper;
    private final BookingMapper bookingMapper;
    private final RoomRepository roomRepository;

    @Override
    @SneakyThrows
    public BookingRs createBookingRoom(RoomRq roomRq) {
        Specification<Room> roomSpecification = Specification.<Room>unrestricted()
                .and(searchRoomById(roomRq.getId()))
                .and(searchRoomByHotelId(roomRq.getRoomBooking().getHotelId()))
                .and(searchRoomByDate(roomRq.getRoomBooking().getStartReserved(), roomRq.getRoomBooking().getEndReserved()))
                .and(searchRoomByClassRoom(roomRq.getRoomBooking().getClassRoomType()))
                .and(searchRoomByBookingId(roomRq.getRoomBooking().getBookingId()));
        List<Room> roomResponses = roomRepository.findAll(roomSpecification);
        if (roomResponses.isEmpty()) {
            throw new BookingException("Rooms by request not found. \n" +
                    "Request: " + objectMapper.writeValueAsString(roomRq.getRoomBooking()));
        }
        Random random = new Random();
        Room room = roomResponses.size() == 1 ? roomResponses.getFirst()
                : roomResponses.get(random.nextInt(roomResponses.size()));
        Booking booking = bookingMapper.bookingFromRoomRequest(roomRq, room);
        booking = bookingRepository.save(booking);
        return bookingMapper.bookingToBookingRs(booking);
    }

    @Override
    public BookingRs updateBookingRoom(RoomRq roomRq) {
        Optional<Booking> bookingOptional = bookingRepository.findById(roomRq.getRoomBooking().getBookingId());
        if (bookingOptional.isEmpty()) {
            throw new BookingException("Booking with id " + roomRq.getRoomBooking().getBookingId() + " not found.");
        }
        try {
            deleteBookingRoom(roomRq);
            return createBookingRoom(roomRq);
        } catch (BookingException e) {
            log.error("Error update booking: ", e);
            bookingRepository.save(bookingOptional.get());
            return BookingRs.builder()
                    .bookingId(roomRq.getRoomBooking().getBookingId())
                    .errorMessage(e.getMessage())
                    .build();
        }
    }

    @Override
    public BookingRs deleteBookingRoom(RoomRq roomRq) {
        bookingRepository.deleteBookingById(roomRq.getRoomBooking().getBookingId());
        if (bookingRepository.existsBookingById(roomRq.getRoomBooking().getBookingId())) {
            throw new BookingException("Booking not remove");
        }
        return BookingRs.builder()
                .bookingId(roomRq.getRoomBooking().getBookingId())
                .description("Booking remove successfully")
                .build();
    }

}
