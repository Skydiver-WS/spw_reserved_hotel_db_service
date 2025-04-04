package ru.project.reserved.system.db.app.service.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.project.reserved.system.db.app.service.dto.room.RoomRequest;
import ru.project.reserved.system.db.app.service.dto.room.RoomResponse;
import ru.project.reserved.system.db.app.service.dto.type.BookingOperationType;
import ru.project.reserved.system.db.app.service.entity.Hotel;
import ru.project.reserved.system.db.app.service.entity.Room;
import ru.project.reserved.system.db.app.service.mapper.RoomMapper;
import ru.project.reserved.system.db.app.service.repository.HotelRepository;
import ru.project.reserved.system.db.app.service.repository.RoomRepository;
import ru.project.reserved.system.db.app.service.service.BookingService;
import ru.project.reserved.system.db.app.service.service.RoomSearchService;
import ru.project.reserved.system.db.app.service.service.RoomService;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final RoomMapper roomMapper;
    private final RoomSearchService searchService;
    private final BookingService bookingService;


    @Override
    public List<RoomResponse> findAllRooms() {
        log.info("Find all rooms");
        return roomMapper.roomsToRoomResponses(roomRepository.findAll());
    }

    @Override
    @Transactional
    @Retryable(
            maxAttempts = 5,
            backoff = @Backoff(delay = 100, multiplier = 3),
            retryFor = {ObjectOptimisticLockingFailureException.class}
    )
    public RoomResponse createRoom(RoomRequest roomRequest) {
        log.info("Create room");
        Hotel hotel = hotelRepository.findById(roomRequest.getHotelId()).
                orElseThrow(() -> new EntityNotFoundException("Hotel with id " + roomRequest.getHotelId() + " not found"));
        Room newRoom = roomMapper.roomResponseToRoom(roomRequest);
        hotel.getRoomList().forEach(room -> {
            Long numCreate = newRoom.getNumberApart();
            if (Objects.equals(room.getNumberApart(), numCreate)) {
                Long num = hotel.getRoomList().stream()
                        .map(Room::getNumberApart) // Извлекаем номера комнат
                        .max(Comparator.naturalOrder()) // Ищем максимальный
                        .orElse(0L);
                newRoom.setNumberApart(num + 1);
            }
        });
        hotel.setCountApart(hotel.getCountApart() + 1); //TODO поправить установку номера комнаты с учетом многопоточки
        newRoom.setHotel(hotel);
        Room room = roomRepository.save(newRoom);
        hotelRepository.save(hotel);
        log.info("Room save successful");
        return roomMapper.roomToRoomResponse(room);
    }

    @Override
    public List<RoomResponse> findRoomsForParameters(RoomRequest request) {
        log.info("Find rooms for parameters");
        List<Room> roomResponses = searchService.searchRoomByParameter(request);
        return roomResponses.isEmpty() ? List.of(RoomResponse.builder()
                .errorMessage("Rooms not found")
                .build()) : roomMapper.roomsToRoomResponses(roomResponses);
    }

    @Override
    public RoomResponse updateRoom(RoomRequest roomRequest) {
        Optional<Room> findRoom = roomRepository.findById(roomRequest.getId());
        if (findRoom.isPresent()) {
            log.info("Update room");
            roomMapper.updateRoom(findRoom.get(), roomRequest);
            Room newRoom = roomRepository.save(findRoom.get());
            return roomMapper.roomToRoomResponse(newRoom);
        }
        return RoomResponse.builder()
                .errorMessage("Room not found")
                .build();
    }

    @Override
    public RoomResponse removeRoom(Long hotelId, Long roomId) {
        if (roomRepository.existsRoomByHotelIdAndId(hotelId, roomId)) {
            roomRepository.deleteByHotelIdAndRoomId(hotelId, roomId);
            log.info("Remove room");
            return RoomResponse.builder()
                    .id(roomId)
                    .description("Room delete")
                    .build();
        }
        return RoomResponse.builder()
                .errorMessage("Room " + roomId + " not found")
                .build();
    }

    @Override
    public RoomResponse reservedRoom(RoomRequest roomRequest) {
        try {
            if (BookingOperationType.UPDATE.equals(roomRequest.getRoomBooking().getOperationType())) {
                return bookingService.updateBookingRoom(roomRequest);
            }
            if (BookingOperationType.DELETE.equals(roomRequest.getRoomBooking().getOperationType())) {
                return bookingService.deleteBookingRoom(roomRequest);
            }
            return bookingService.createBookingRoom(roomRequest);
        } catch (Exception e) {
            log.error(e.getMessage());
            return RoomResponse.builder()
                    .errorMessage(e.getMessage())
                    .classRoomType(roomRequest.getClassRoomType())
                    .build();
        }
    }
}
