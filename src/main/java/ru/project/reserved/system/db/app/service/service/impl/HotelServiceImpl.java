package ru.project.reserved.system.db.app.service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.PersistentObjectException;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.project.reserved.system.db.app.service.dto.hotel.HotelRequest;
import ru.project.reserved.system.db.app.service.dto.hotel.HotelResponse;
import ru.project.reserved.system.db.app.service.entity.Hotel;
import ru.project.reserved.system.db.app.service.mapper.HotelMapper;
import ru.project.reserved.system.db.app.service.repository.HotelRepository;
import ru.project.reserved.system.db.app.service.service.HotelSearchService;
import ru.project.reserved.system.db.app.service.service.HotelService;

import java.io.IOException;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;
    private final HotelSearchService hotelSearchService;

    @Override
    @SneakyThrows
    public HotelResponse getAllHotels() {
        log.info("Get all hotels");
        try {
            return HotelResponse.builder()
                    .hotels(hotelMapper.mappingHotelListToHotelResponseList(hotelRepository.findAll()))
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return HotelResponse.builder()
                    .errorMessage(e.getMessage())
                    .build();
        }
    }

    @Override
    public HotelResponse getAllHotelsByParams(HotelRequest request) {
        log.info("Get hotels by params");
        return HotelResponse.builder()
                .hotels(hotelSearchService.searchHotels(request))
                .build();
    }

    @Override
    @SneakyThrows
    @Retryable(
            retryFor = {IncorrectResultSizeDataAccessException.class, DataAccessException.class, IOException.class,
            PersistentObjectException.class},
            maxAttempts = 5,
            backoff = @Backoff(delay = 500, multiplier = 1)
    )
    @Transactional
    public HotelResponse createHotel(HotelRequest hotelRequest) {
        log.info("Create hotel {}", hotelRequest.getName());
        Hotel hotel = hotelMapper.mappingHotelRequestToHotel(hotelRequest);
        Hotel newHotel = hotelRepository.saveAndFlush(hotel);
        log.info("Hotel: {} save successful", newHotel.getName());
        return hotelMapper.mappingHotelToHotelRequest(newHotel);
    }

    @Override
    public HotelResponse updateHotel(HotelRequest hotelRequest) {
        log.info("Update hotel");
        Optional<Hotel> hotelOptional = hotelRepository.findById(hotelRequest.getId());
        if (hotelOptional.isEmpty()) {
            return HotelResponse.builder()
                    .id(hotelRequest.getId())
                    .errorMessage("Hotel with id " + hotelRequest.getId() + " not found")
                    .build();
        }
        Hotel hotel = hotelOptional.get();
        hotelMapper.updateHotelByHotelRequest(hotel, hotelRequest);
        try {
            Hotel updatedHotel;
            if (hotelRequest.getPhotos() != null) {
                hotel.setPhotos(hotelRequest.getPhotos());
            } else {
                hotel.setPhotos(hotelOptional.get().getPhotos());
            }
                updatedHotel = hotelRepository.save(hotel);
            return hotelMapper.mappingHotelToHotelRequest(updatedHotel);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            ex.printStackTrace();
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
