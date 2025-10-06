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
import ru.project.reserved.system.db.app.service.dto.hotel.HotelRq;
import ru.project.reserved.system.db.app.service.dto.hotel.HotelRs;
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
    public HotelRs getAllHotels() {
        log.info("Get all hotels");
        try {
            return HotelRs.builder()
                    .hotels(hotelMapper.mappingHotelListToHotelResponseList(hotelRepository.findAll()))
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return HotelRs.builder()
                    .errorMessage(e.getMessage())
                    .build();
        }
    }

    @Override
    public HotelRs getAllHotelsByParams(HotelRq request) {
        log.info("Get hotels by params");
        return HotelRs.builder()
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
    public HotelRs createHotel(HotelRq hotelRq) {
        log.info("Create hotel {}", hotelRq.getName());
        Hotel hotel = hotelMapper.mappingHotelRequestToHotel(hotelRq);
        Hotel newHotel = hotelRepository.save(hotel);
        log.info("Hotel: {} save successful", newHotel.getName());
        return hotelMapper.mappingHotelToHotelRequest(newHotel);
    }

    @Override
    public HotelRs updateHotel(HotelRq hotelRq) {
        log.info("Update hotel");
        Optional<Hotel> hotelOptional = hotelRepository.findById(hotelRq.getId());
        if (hotelOptional.isEmpty()) {
            return HotelRs.builder()
                    .id(hotelRq.getId())
                    .errorMessage("Hotel with id " + hotelRq.getId() + " not found")
                    .build();
        }
        Hotel hotel = hotelOptional.get();
        hotelMapper.updateHotelByHotelRequest(hotel, hotelRq);
        try {
            Hotel updatedHotel;
            if (hotelRq.getPhotos() != null) {
                hotel.setPhotos(hotelRq.getPhotos());
            } else {
                hotel.setPhotos(hotelOptional.get().getPhotos());
            }
                updatedHotel = hotelRepository.save(hotel);
            return hotelMapper.mappingHotelToHotelRequest(updatedHotel);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            ex.printStackTrace();
            return HotelRs.builder()
                    .name(hotelRq.getName())
                    .errorMessage(ex.getMessage())
                    .build();
        }

    }

    @Override
    public HotelRs deleteHotel(HotelRq hotelRq) {
        log.info("Delete hotel");
        Optional<Hotel> hotel = hotelRepository.findById(hotelRq.getId());
        if (hotel.isEmpty()) {
            log.info("Hotel with id {} not found", hotelRq.getId());
            return HotelRs.builder()
                    .id(hotelRq.getId())
                    .errorMessage("Hotel with id " + hotelRq.getId() + " not found")
                    .build();
        }
        hotelRepository.deleteById(hotelRq.getId());
        return HotelRs.builder()
                .id(hotelRq.getId())
                .message("Hotel with id " + hotelRq.getId() + " delete")
                .build();
    }
}
