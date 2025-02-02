package ru.project.reserved.system.db.app.service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.project.reserved.system.db.app.service.dto.room.RoomRequest;
import ru.project.reserved.system.db.app.service.dto.room.RoomResponse;
import ru.project.reserved.system.db.app.service.dto.type.SortType;
import ru.project.reserved.system.db.app.service.entity.Hotel;
import ru.project.reserved.system.db.app.service.entity.Room;
import ru.project.reserved.system.db.app.service.mapper.RoomMapper;
import ru.project.reserved.system.db.app.service.repository.HotelRepository;
import ru.project.reserved.system.db.app.service.repository.RoomRepository;
import ru.project.reserved.system.db.app.service.service.RoomSearchService;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomSearchServiceImpl implements RoomSearchService {
    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private RoomMapper roomMapper;

    @Override
    public List<RoomResponse> searchRoomByParameter(RoomRequest roomRequest) {
        Hotel hotel = hotelRepository.findById(roomRequest.getRoomSearch().getHotelId()).orElse(null);
        List<Room> rooms = roomRepository.findRoomsByHotel(hotel);
        filterByClassRoom(rooms, roomRequest);
        filterByClassRoom(rooms, roomRequest);
        filterByCoast(rooms, roomRequest);
        filterByClassRoom(rooms, roomRequest);
        return roomMapper.roomsToRoomResponses(rooms);
    }

    private void filterByDateReserved(List<Room> rooms, RoomRequest roomRequest) {
        rooms.removeIf(r -> r.getStartReserved().after(roomRequest.getRoomSearch().getStartReserved())
                && r.getEndReserved().before(roomRequest.getRoomSearch().getEndReserved()));
    }

    private void filterByCoast(List<Room> rooms, RoomRequest roomRequest) {
        rooms.removeIf(r -> Objects.nonNull(roomRequest.getRoomSearch().getCoast())
                && r.getCoast() > roomRequest.getRoomSearch().getCoast());

        Comparator<Room> comparator = Comparator.comparingDouble(Room::getCoast);
        if (Objects.nonNull(roomRequest.getRoomSearch().getCoast())
                && roomRequest.getRoomSearch().getSortCoast().equals(SortType.DESC)) {
            comparator = comparator.reversed();
        }
        rooms.sort(comparator);
    }

    private void filterByClassRoom(List<Room> rooms, RoomRequest roomRequest) {
        rooms.removeIf(r -> Objects.nonNull(roomRequest.getRoomSearch()
                .getClassType()) && !r.equals(roomRequest.getRoomSearch()
                .getClassType()));
    }
}
