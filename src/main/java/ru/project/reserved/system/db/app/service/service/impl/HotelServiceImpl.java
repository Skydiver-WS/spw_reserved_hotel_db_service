package ru.project.reserved.system.db.app.service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import ru.project.reserved.system.db.app.service.dto.HotelRequest;
import ru.project.reserved.system.db.app.service.dto.HotelResponse;
import ru.project.reserved.system.db.app.service.dto.SortType;
import ru.project.reserved.system.db.app.service.entity.City;
import ru.project.reserved.system.db.app.service.entity.Hotel;
import ru.project.reserved.system.db.app.service.mapper.HotelMapper;
import ru.project.reserved.system.db.app.service.repository.HotelRepository;
import ru.project.reserved.system.db.app.service.service.HotelService;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;

    @Override
    @SneakyThrows
    public List<HotelResponse> getAllHotels() {
        log.info("Get all hotels");
        try {
        return hotelMapper.mappingHotelListToHotelResponseList(hotelRepository.findAll());
        } catch (Exception e) {
            log.error(e.getMessage());
            return List.of(HotelResponse.builder()
                    .errorMessage(e.getMessage())
                    .build());
        }
    }

    @Override
    @SneakyThrows
    public List<HotelResponse> getAllHotelsByParam(HotelRequest hotelRequest) {
        log.info("Get all hotels by params");
        if (Strings.isNotBlank(hotelRequest.getName())) {
            log.info("Get all hotels by params name: {}", hotelRequest.getName());
            return hotelMapper.mappingHotelListToHotelResponseList(hotelRepository
                    .findHotelsByName(hotelRequest.getName()));
        }
        if (!hotelRequest.getCityList().isEmpty()) {
            log.info("Get all hotels by params cityList: {}", hotelRequest.getCityList());
            return hotelMapper.mappingHotelListToHotelResponseList(hotelRepository
                    .findHotelsByCityList(new HashSet<>(hotelRequest.getCityList())));
        }
        if (Objects.nonNull(hotelRequest.getRating())) {
            log.info("Get all hotels by params rating: {}", hotelRequest.getRating());
            List<Hotel> hotels = hotelRepository.findAll();
            hotels = sortedByRating(hotels, hotelRequest);
            return hotelMapper.mappingHotelListToHotelResponseList(hotels);
        }
        if (Objects.nonNull(hotelRequest.getDistance())) {
            log.info("Get all hotels by params distance: {}", hotelRequest.getDistance());
            List<Hotel> hotels = hotelRepository.findHotelsByDistance(hotelRequest.getDistance())
                    .stream()
                    .sorted(Comparator.comparing(Hotel::getDistance))
                    .toList();
            return hotelMapper.mappingHotelListToHotelResponseList(hotels);
        }
        return List.of(HotelResponse.builder()
                .errorMessage("Hotels not found!")
                .build());
    }

    @Override
    @SneakyThrows
    public HotelResponse createHotel(HotelRequest hotelRequest) {
        Hotel hotel = hotelMapper.mappingHotelRequestToHotel(hotelRequest);
        log.info("Create hotel {}", hotel.getName());
        try {
            Hotel newHotel = hotelRepository.save(hotel);
            return hotelMapper.mappingHotelToHotelRequest(newHotel);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return HotelResponse.builder()
                    .name(hotelRequest.getName())
                    .errorMessage(ex.getMessage())
                    .build();
        }
    }

    @Override
    public HotelResponse updateHotel(HotelRequest hotelRequest) {
        log.info("Update hotel");
        Optional<Hotel> hotel = hotelRepository.findById(hotelRequest.getId());
        if (hotel.isEmpty()) {
            return HotelResponse.builder()
                    .id(hotelRequest.getId())
                    .errorMessage("Hotel with id" +  hotelRequest.getId() + "not found")
                    .build();
        }
        hotelMapper.updateHotelByHotelRequest(hotel.get(), hotelRequest);
        try {
            Hotel updatedHotel = hotelRepository.save(hotel.get());
            return hotelMapper.mappingHotelToHotelRequest(updatedHotel);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return HotelResponse.builder()
                    .name(hotelRequest.getName())
                    .errorMessage(ex.getMessage())
                    .build();
        }

    }

    @Override
    public HotelResponse deleteHotel(HotelRequest hotelRequest) {
        log.info("Delete hotel");
        Optional<Hotel> hotel = hotelRepository.findById(hotelRequest.getId());
        if (hotel.isEmpty()) {
            log.info("Hotel with id {} not found", hotelRequest.getId());
            return HotelResponse.builder()
                    .id(hotelRequest.getId())
                    .errorMessage("Hotel with id " + hotelRequest.getId() + " not found")
                    .build();
        }
        hotelRepository.deleteById(hotelRequest.getId());
        return HotelResponse.builder()
                .id(hotelRequest.getId())
                .message("Hotel with id " + hotelRequest.getId() + " delete")
                .build();
    }

    private List<Hotel> sortedByRating(List<Hotel> hotels, HotelRequest hotelRequest) {
        return hotels.stream()
                .filter(r -> {
                    if (hotelRequest.getParameters().getSortType().equals(SortType.DESC)){
                        return r.getRating() <= hotelRequest.getRating();
                    } else if (hotelRequest.getParameters().getSortType().equals(SortType.ASC)){
                        return r.getRating() >= hotelRequest.getRating();
                    }
                    return Objects.equals(r.getRating(), hotelRequest.getRating());
                }).sorted(hotelRequest.getParameters().getSortType().equals(SortType.DESC)
                        ? Comparator.comparing(Hotel::getRating).reversed()
                        : Comparator.comparing(Hotel::getRating)).toList();
    }
}
