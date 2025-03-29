package ru.project.reserved.system.db.app.service.service.impl;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.project.reserved.system.db.app.service.dto.room.RoomRequest;
import ru.project.reserved.system.db.app.service.dto.room.RoomResponse;
import ru.project.reserved.system.db.app.service.dto.type.ClassRoomType;
import ru.project.reserved.system.db.app.service.dto.type.SortType;
import ru.project.reserved.system.db.app.service.entity.Hotel;
import ru.project.reserved.system.db.app.service.entity.Room;
import ru.project.reserved.system.db.app.service.exception.BookingException;
import ru.project.reserved.system.db.app.service.mapper.RoomMapper;
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

    @Override
    public List<Room> searchRoomByParameter(RoomRequest roomRequest) {
        return findAndFilterRooms(roomRequest);
    }

    @Override
    public List<Room> searchRoomByParameterForReserved(RoomRequest roomRequest) {
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
        List<Room> rooms = roomRepository.findRoomsByHotel(hotel);
        filterByClassRoom(rooms, classType);
        filterByCoast(rooms, coast, sortCoast);
        filterByDateReserved(rooms, startReserved, endReserved);
        return rooms;
    }

    private void filterByDateReserved(List<Room> rooms, Date startReserved, Date endReserved) {
        rooms.removeIf(room -> room.getBookings().stream()
                .anyMatch(booking ->
                        (startReserved.before(booking.getEndReserved()) && endReserved.after(booking.getStartReserved()))
                )
        );
    }


    private void filterByCoast(List<Room> rooms, Double coast, SortType sortCoast) {
        rooms.removeIf(r -> (Objects.nonNull(coast) && coast != 0.0)
                && r.getCoast() > coast);

        Comparator<Room> comparator = Comparator.comparingDouble(Room::getCoast);
        if (Objects.nonNull(coast)
                && sortCoast.equals(SortType.DESC)) {
            comparator = comparator.reversed();
        }
        rooms.sort(comparator);
    }

    private void filterByClassRoom(List<Room> rooms, ClassRoomType classType) {
        rooms.removeIf(r -> Objects.nonNull(classType) && !r.getClassRoomType().equals(classType));
    }
}
