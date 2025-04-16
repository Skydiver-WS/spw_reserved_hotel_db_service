package ru.project.reserved.system.db.app.service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.project.reserved.system.db.app.service.dto.hotel.HotelRequest;
import ru.project.reserved.system.db.app.service.dto.hotel.HotelResponse;
import ru.project.reserved.system.db.app.service.dto.room.RoomRequest;
import ru.project.reserved.system.db.app.service.dto.room.RoomResponse;
import ru.project.reserved.system.db.app.service.dto.type.SortType;
import ru.project.reserved.system.db.app.service.entity.City;
import ru.project.reserved.system.db.app.service.entity.Hotel;
import ru.project.reserved.system.db.app.service.entity.Room;
import ru.project.reserved.system.db.app.service.mapper.HotelMapper;
import ru.project.reserved.system.db.app.service.repository.BookingRepository;
import ru.project.reserved.system.db.app.service.repository.HotelRepository;
import ru.project.reserved.system.db.app.service.repository.RoomRepository;
import ru.project.reserved.system.db.app.service.service.BookingService;
import ru.project.reserved.system.db.app.service.service.HotelSearchService;
import ru.project.reserved.system.db.app.service.service.RoomSearchService;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class HotelSearchServiceImpl implements HotelSearchService {

    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;
    private final RoomRepository roomRepository;
    private final BookingRepository bookingRepository;

    @Override
    public List<HotelResponse> searchHotels(@Validated HotelRequest request) {
        List<Hotel> hotels = hotelRepository.findHotelsByCity_Name(request.getHotelSearch().getCity());
        findAndSortByName(hotels, request);
        findAndSortByDate(hotels, request);
        findAndSortByDistance(hotels, request);
        findAndSortByRating(hotels, request);
        findAndSortByCoast(hotels, request);
        return hotelMapper.mappingHotelListToHotelResponseList(hotels);
    }


    private void findAndSortByName(List<Hotel> hotels, HotelRequest request) {
        log.info("Sorted by name");
        hotels.removeIf(h -> Strings.isNotBlank(request.getHotelSearch().getHotelName())
                && !h.getName().equals(request.getHotelSearch().getHotelName()));
    }

    private void findAndSortByDate(List<Hotel> hotels, HotelRequest request) {
        log.info("Sorted by date");
        List<Long[]> rooms = roomRepository.findRoomIdsByHotelIdIn(hotels.stream().map(Hotel::getId).toList());
        List<Long> bookingIds = bookingRepository
                .findRoomsIdsByRoomIdInAndDateNotOverlapping(rooms.stream().map(l -> l[0]).toList(),
                        request.getHotelSearch().getStartReserved(),
                        request.getHotelSearch().getEndReserved());
        rooms.removeIf(r -> !bookingIds.isEmpty() && bookingIds.contains(r[0]));
        List<Long> hotelIds = rooms.stream().map(r -> r[1]).toList();
        hotels.removeIf(h -> !hotelIds.contains(h.getId()));
        log.info("Sorted by date");
    }

    private void findAndSortByDistance(List<Hotel> hotels, HotelRequest request) {
        log.info("Sorted by distance");
        hotels.removeIf(h -> Objects.nonNull(request.getHotelSearch().getDistance())
                && h.getDistance() > request.getHotelSearch().getDistance());
    }

    private void findAndSortByRating(List<Hotel> hotels, HotelRequest request) {
        log.info("Sorted by rating");
        hotels.removeIf(h -> Objects.nonNull(request.getHotelSearch().getRating())
                && h.getRating() < request.getHotelSearch().getRating());
    }

    private void findAndSortByCoast(List<Hotel> hotels, HotelRequest request) {
        log.info("Sorted by coast");
        Long coastMinL = request.getHotelSearch().getCoastMin();
        Long coastMaxL = request.getHotelSearch().getCoastMax();

        coastMaxL = Objects.isNull(coastMaxL) ? Long.MAX_VALUE : coastMaxL;
        coastMinL = Objects.isNull(coastMinL) ? 0 : coastMinL;

        Double minCoast = Double.valueOf(coastMinL);
        Double maxCoast = Double.valueOf(coastMaxL);
        List<Object[]> roomsCoast = roomRepository.findRoomsByCoast(hotels.stream().map(Hotel::getId).toList());
        List<Room> rooms = roomsCoast.stream()
                .map(l -> Room.builder()
                        .id((Long) l[0])
                        .coast((Double) l[2])
                        .hotel(Hotel.builder()
                                .id((Long) l[1])
                                .build())
                        .build())
                .filter(r -> {
                    Double coast = r.getCoast();
                    return coast >= minCoast &&
                            coast <= maxCoast;
                })
                .toList();
        Map<Long, List<Room>> hotelRoomsMap = rooms.stream()
                .collect(Collectors.groupingBy(r -> r.getHotel().getId()));
        hotels.removeIf(h -> !hotelRoomsMap.containsKey(h.getId()));
        hotels.forEach(h -> {
            h.setRoomList(hotelRoomsMap.get(h.getId()));
        });
    }
}
