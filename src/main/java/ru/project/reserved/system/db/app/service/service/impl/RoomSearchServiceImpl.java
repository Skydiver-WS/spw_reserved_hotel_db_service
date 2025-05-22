package ru.project.reserved.system.db.app.service.service.impl;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.project.reserved.system.db.app.service.dto.room.RoomRequest;
import ru.project.reserved.system.db.app.service.dto.room.RoomResponse;
import ru.project.reserved.system.db.app.service.dto.type.ClassRoomType;
import ru.project.reserved.system.db.app.service.dto.type.SortType;
import ru.project.reserved.system.db.app.service.entity.Booking;
import ru.project.reserved.system.db.app.service.entity.Hotel;
import ru.project.reserved.system.db.app.service.entity.Room;
import ru.project.reserved.system.db.app.service.exception.BookingException;
import ru.project.reserved.system.db.app.service.mapper.RoomMapper;
import ru.project.reserved.system.db.app.service.repository.BookingRepository;
import ru.project.reserved.system.db.app.service.repository.HotelRepository;
import ru.project.reserved.system.db.app.service.repository.RoomRepository;
import ru.project.reserved.system.db.app.service.service.RoomSearchService;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomSearchServiceImpl implements RoomSearchService {
    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final BookingRepository bookingRepository;
    private static final double EPSILON = 1e-10;

    @Override
    public List<Room> searchRoomByParameter(RoomRequest roomRequest) {
        log.info("Search rooms");
        return findAndFilterRooms(roomRequest);
    }

    @Override
    public List<Room> searchRoomByParameterForReserved(RoomRequest roomRequest) {
        log.info("Search rooms by reserved");
        return findAndFilterRooms(roomRequest);
    }

    private List<Room> findAndFilterRooms(RoomRequest roomRequest) {
        Long hotelId;
        Date startReserved;
        Date endReserved;
        ClassRoomType classType;
        SortType sortCoast = SortType.ASC;
        Double coast = 0d;
        if (Objects.nonNull(roomRequest.getRoomSearch())) {
            hotelId = roomRequest.getRoomSearch().getHotelId();
            startReserved = roomRequest.getRoomSearch().getStartReserved();
            endReserved = roomRequest.getRoomSearch().getEndReserved();
            classType = roomRequest.getRoomSearch().getClassRoomType();
            sortCoast = roomRequest.getRoomSearch().getSortCoast();
            coast = roomRequest.getRoomSearch().getCoast();
        } else {
            hotelId = roomRequest.getRoomBooking().getHotelId();
            startReserved = roomRequest.getRoomBooking().getStartReserved();
            endReserved = roomRequest.getRoomBooking().getEndReserved();
            classType = roomRequest.getRoomBooking().getClassType();
            if(startReserved == null || endReserved == null) {
                log.error("Start date reserved {} or end date reserved {} is null", startReserved, endReserved);
                throw  new BookingException("Not date reserved");
            }
        }
        Hotel hotel = hotelRepository.findById(hotelId).orElse(null);
        List<Long> roomIds = roomRepository.findIdsRoomByHotel(hotel);
        filterByClassRoom(roomIds, classType);
        filterByCoast(roomIds, coast);
        filterByDateReserved(roomIds, startReserved, endReserved);
        return roomRepository.findRoomsByIds(roomIds);
    }

    private void filterByClassRoom(List<Long> roomIds, ClassRoomType classType) {
        if (Objects.nonNull(classType)) {
            List<Long> roomIdsByClassRoom  = roomRepository.findIdsRoomsByClassRoom(classType);
            roomIds.removeIf(r -> !roomIdsByClassRoom.contains(r));
        }
    }

    private void filterByCoast(List<Long> roomIds, Double coast) {
        if (Objects.nonNull(coast) && !coast.equals(0d) && Math.abs(coast) > EPSILON) {
            List<Long> roomIdsByCoast = roomRepository.findIdsRoomsByCoast(coast);
            roomIds.removeIf(r -> !roomIdsByCoast.contains(r));
        }
    }


    private void filterByDateReserved(List<Long> roomIds, Date startReserved, Date endReserved) {
        List<Long> bookingRoomIds = bookingRepository.findRoomsIdsByRoomIdInAndDateNotOverlapping(roomIds, startReserved, endReserved);
        roomIds.removeIf(r -> !bookingRoomIds.contains(r));
    }
}
