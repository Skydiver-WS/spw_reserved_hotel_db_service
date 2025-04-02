package ru.project.reserved.system.db.app.service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.NonUniqueResultException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.project.reserved.system.db.app.service.aop.SearchEntity;
import ru.project.reserved.system.db.app.service.dto.hotel.HotelRequest;
import ru.project.reserved.system.db.app.service.dto.hotel.HotelResponse;
import ru.project.reserved.system.db.app.service.entity.City;
import ru.project.reserved.system.db.app.service.entity.Hotel;
import ru.project.reserved.system.db.app.service.mapper.HotelMapper;
import ru.project.reserved.system.db.app.service.repository.CityRepository;
import ru.project.reserved.system.db.app.service.repository.HotelRepository;
import ru.project.reserved.system.db.app.service.service.HotelSearchService;
import ru.project.reserved.system.db.app.service.service.HotelService;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final CityRepository cityRepository;
    private final HotelMapper hotelMapper;
    private final HotelSearchService hotelSearchService;

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
    public List<HotelResponse> getAllHotelsByParams(HotelRequest request) {
        log.info("Get hotels by params");
        return hotelSearchService.searchHotels(request);
    }

    @Override
    @SneakyThrows
    @SearchEntity
    @Transactional
    @Retryable(
            retryFor = {NonUniqueResultException.class},
            maxAttempts = 5,
            backoff = @Backoff(delay = 100, multiplier = 3)
    )
    public HotelResponse createHotel(HotelRequest hotelRequest) {
        Hotel hotel = hotelMapper.mappingHotelRequestToHotel(hotelRequest);
        log.info("Create hotel {}", hotel.getName());
        try {
            Optional<City> city = cityRepository.findCityByName(hotelRequest.getCity().getName());
            city.ifPresent(value -> hotel.setCityList(Set.of(value)));
            Hotel newHotel = hotelRepository.save(hotel);
            return hotelMapper.mappingHotelToHotelRequest(newHotel);
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            return HotelResponse.builder()
                    .name(hotelRequest.getName())
                    .errorMessage(ex.getMessage())
                    .build();
        }
    }

    @Override
    public HotelResponse updateHotel(HotelRequest hotelRequest) {
        log.info("Update hotel");
        Optional<Hotel> hotelOptional = hotelRepository.findById(hotelRequest.getId());
        if (hotelOptional.isEmpty()) {
            return HotelResponse.builder()
                    .id(hotelRequest.getId())
                    .errorMessage("Hotel with id" + hotelRequest.getId() + "not found")
                    .build();
        }
        Hotel hotel = hotelOptional.get();
        hotelMapper.updateHotelByHotelRequest(hotel, hotelRequest);
        try {
            Hotel updatedHotel;
            if (hotelRequest.getPhotos() != null) {
                hotel.setPhotos(hotelRequest.getPhotos());
                updatedHotel = hotelRepository.save(hotel);
            } else {
                hotel.setPhotos(hotelOptional.get().getPhotos());
                updatedHotel = hotelRepository.save(hotel);
            }
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
}
