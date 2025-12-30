package ru.project.reserved.system.db.app.service.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.NonUniqueResultException;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.transaction.annotation.Transactional;
import ru.project.reserved.system.db.app.service.dto.booking.BookingRs;
import ru.project.reserved.system.db.app.service.dto.room.RoomRq;
import ru.project.reserved.system.db.app.service.dto.room.RoomRs;
import ru.project.reserved.system.db.app.service.dto.type.BookingOperationType;
import ru.project.reserved.system.db.app.service.dto.type.MetricType;
import ru.project.reserved.system.db.app.service.entity.Hotel;
import ru.project.reserved.system.db.app.service.entity.Photo;
import ru.project.reserved.system.db.app.service.entity.Room;
import ru.project.reserved.system.db.app.service.mapper.RoomMapper;
import ru.project.reserved.system.db.app.service.repository.HotelRepository;
import ru.project.reserved.system.db.app.service.repository.RoomRepository;
import ru.project.reserved.system.db.app.service.service.BookingService;
import ru.project.reserved.system.db.app.service.service.MetricService;
import ru.project.reserved.system.db.app.service.service.RoomSearchService;
import ru.project.reserved.system.db.app.service.service.RoomService;

import java.io.IOException;
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
    private final MetricService metric;


    @Override
    public RoomRs findAllRooms() {
        log.info("Find all rooms");
        return RoomRs.builder()
                .rooms(roomMapper.roomsToRoomResponses(roomRepository.findAll()))
                .build();
    }

    @Override
    @Retryable(
            maxAttempts = 10,
            backoff = @Backoff(delay = 500, multiplier = 2),
            retryFor = {ObjectOptimisticLockingFailureException.class,
                    DataAccessException.class,
                    IOException.class,
                    NonUniqueResultException.class,
                    CannotCreateTransactionException.class}
    )
    @Transactional
    public RoomRs createRoom(RoomRq roomRq) {
        log.info("Create room");
        Hotel hotel = hotelRepository.findById(roomRq.getHotelId()).
                orElseThrow(() -> new EntityNotFoundException("Hotel with id " + roomRq.getHotelId() + " not found"));
        Room newRoom = roomMapper.roomResponseToRoom(roomRq, hotel);
        newRoom.getPhotoList().forEach(p -> p.setRoom(newRoom));
        Room room = roomRepository.saveAndFlush(newRoom);
        log.info("Room save successful");
        return roomMapper.roomToRoomResponse(room);
    }

    @Override
    public RoomRs findRoomsForParameters(RoomRq request) {
        log.info("Find rooms for parameters");
        List<Room> roomResponses = searchService.searchRoomByParameter(request);
        return RoomRs.builder()
                .rooms(roomResponses.isEmpty() ? List.of(RoomRs.builder()
                        .errorMessage("Rooms not found")
                        .build()) : roomMapper.roomsToRoomResponses(roomResponses))
                .build();
    }

    @Override
    public RoomRs updateRoom(RoomRq roomRq) {
        Optional<Room> findRoom = roomRepository.findRoomByIdAndHotel_Id(roomRq.getId(), roomRq.getHotelId());
        if (findRoom.isEmpty()) {
            log.warn("Room with id {} not found", roomRq.getId());
            return RoomRs.builder()
                    .errorMessage("Room not found")
                    .build();
        }
        log.info("Update room");
        Room room = findRoom.get();
        roomMapper.updateRoom(room, roomRq);
        if (Objects.nonNull(roomRq.getPhotoList())) {
            List<Photo> photoList = room.getPhotoList();
            roomRq.getPhotoList().forEach(p -> p.setRoom(room));
            photoList.addAll(room.getPhotoList());
            room.setPhotoList(photoList);
        }
        Room newRoom = roomRepository.save(room);
        return roomMapper.roomToRoomResponse(newRoom);

    }

    @Override
    public RoomRs removeRoom(RoomRq request) {
        if (roomRepository.existsRoomByHotelIdAndId(request.getHotelId(), request.getId())) {
            roomRepository.deleteByHotelIdAndRoomId(request.getHotelId(), request.getId());
            log.info("Remove room");
            return RoomRs.builder()
                    .id(request.getId())
                    .description("Room delete")
                    .build();
        }
        return RoomRs.builder()
                .errorMessage("Room " + request.getId() + " not found")
                .build();
    }

    @Override
    public BookingRs reservedRoom(RoomRq roomRq) {
        try {
            BookingRs rs = null;
            if (BookingOperationType.UPDATE.equals(roomRq.getRoomBooking().getOperationType())) {
               rs = bookingService.updateBookingRoom(roomRq);
                metric.sendMetricEnd(MetricType.UPDATE_RESERVATION, rs, "Update reserved");
                return rs;
            }
            if (BookingOperationType.DELETE.equals(roomRq.getRoomBooking().getOperationType())) {
                rs = bookingService.deleteBookingRoom(roomRq);
                metric.sendMetricEnd(MetricType.DELETE_RESERVATION, rs, "Delete reserved");
                return rs;
            }
            rs = bookingService.createBookingRoom(roomRq);
            metric.sendMetricEnd(MetricType.CREATE_RESERVATION, rs, "Create reserved");
            return rs;
        } catch (Exception e) {
            log.error("Error operation booking: ", e);
            metric.sendExceptionMetric(MetricType.ERROR, e, e.getMessage());
            return BookingRs.builder()
                    .errorMessage(e.getMessage())
                    .classRoomType(roomRq.getClassRoomType())
                    .build();
        }
    }
}
