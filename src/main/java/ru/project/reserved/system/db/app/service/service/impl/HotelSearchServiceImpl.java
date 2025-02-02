package ru.project.reserved.system.db.app.service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.project.reserved.system.db.app.service.dto.hotel.HotelRequest;
import ru.project.reserved.system.db.app.service.dto.hotel.HotelResponse;
import ru.project.reserved.system.db.app.service.dto.type.SortType;
import ru.project.reserved.system.db.app.service.entity.City;
import ru.project.reserved.system.db.app.service.entity.Hotel;
import ru.project.reserved.system.db.app.service.entity.Room;
import ru.project.reserved.system.db.app.service.mapper.HotelMapper;
import ru.project.reserved.system.db.app.service.repository.HotelRepository;
import ru.project.reserved.system.db.app.service.service.HotelSearchService;

import java.util.*;

@RequiredArgsConstructor
@Slf4j
@Service
public class HotelSearchServiceImpl implements HotelSearchService {

    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;

    @Override
    public List<HotelResponse> searchHotels(@Validated HotelRequest request) {
        List<Hotel> hotels = hotelRepository.findHotelsByCityList(Set.of(City.builder()
                .name(request.getHotelSearch().getCity())
                .build()));
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
        hotels.removeIf(h -> h.getRoomList()
                .removeIf(r -> r.getStartReserved().after(request.getHotelSearch().getStartReserved())
                        && r.getEndReserved().before(request.getHotelSearch().getEndReserved())));
    }

    private void findAndSortByDistance(List<Hotel> hotels, HotelRequest request) {
        log.info("Sorted by distance");
        hotels.removeIf(h -> Objects.nonNull(request.getDistance()) && h.getDistance() > request.getDistance());
        Comparator<Hotel> comparator = Comparator.comparingDouble(Hotel::getDistance);
        if (Objects.nonNull(request.getDistance()) && request.getHotelSearch().getSortDistance().equals(SortType.DESC)) {
            comparator = comparator.reversed();
        }
        hotels.sort(comparator);

    }

    private void findAndSortByRating(List<Hotel> hotels, HotelRequest request) {
        log.info("Sorted by rating");
        hotels.removeIf(h -> Objects.nonNull(request.getRating()) && h.getRating() < request.getRating());
        Comparator<Hotel> comparator = Comparator.comparingDouble(Hotel::getRating);
        if (Objects.nonNull(request.getHotelSearch().getSortRating())
                && request.getHotelSearch().getSortRating().equals(SortType.DESC)) {
            comparator = comparator.reversed();
        }
        hotels.sort(comparator);
    }

    private void findAndSortByCoast(List<Hotel> hotels, HotelRequest request) {
        log.info("Sorted by coast");
        hotels.removeIf(h -> {
            List<Room> filteredRooms = h.getRoomList().stream()
                    .filter(room -> Objects.nonNull(request.getHotelSearch().getCoastMin())
                            && room.getCoast() >= request.getHotelSearch().getCoastMin())
                    .filter(room -> Objects.nonNull(request.getHotelSearch().getCoastMax())
                            && room.getCoast() <= request.getHotelSearch().getCoastMax())
                    .toList();
            h.setRoomList(filteredRooms);
            return filteredRooms.isEmpty();
        });
    }


}
