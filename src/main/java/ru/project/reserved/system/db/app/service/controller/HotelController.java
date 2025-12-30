package ru.project.reserved.system.db.app.service.controller;

import io.micrometer.core.instrument.Counter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.project.reserved.system.db.app.service.aop.Metric;
import ru.project.reserved.system.db.app.service.dto.hotel.HotelRq;
import ru.project.reserved.system.db.app.service.dto.hotel.HotelRs;
import ru.project.reserved.system.db.app.service.dto.type.MetricType;
import ru.project.reserved.system.db.app.service.service.HotelService;

@RestController
@RequestMapping("/api/v1/hotel")
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;
    private final Counter counter;

    @GetMapping
    @Metric(type = MetricType.ALL_HOTELS, description = "Get all hotels")
    public ResponseEntity<HotelRs> findAllHotels(Pageable pageable){
        return ResponseEntity.ok(hotelService.getAllHotels(pageable));
    }

    @PostMapping("/search")
    @Metric(type = MetricType.SEARCH_HOTEL, description = "Search hotel")
    public ResponseEntity<HotelRs> findHotels(@RequestBody HotelRq hotelRq){
        return ResponseEntity.ok(hotelService.getAllHotelsByParams(hotelRq));
    }

    @PostMapping
    @Metric(type = MetricType.CREATE_HOTEL, description = "Create hotel")
    public ResponseEntity<HotelRs> createHotel(@Validated @RequestBody HotelRq hotelRq){
        counter.increment();
        return ResponseEntity.ok(hotelService.createHotel(hotelRq));
    }

    @PutMapping
    @Metric(type = MetricType.UPDATE_HOTEL, description = "Update hotel")
    public ResponseEntity<HotelRs> updateHotel(@Validated @RequestBody HotelRq hotelRq){
        return ResponseEntity.ok(hotelService.updateHotel(hotelRq));
    }

    @DeleteMapping
    @Metric(type = MetricType.DELETE_HOTEL, description = "Delete hotel")
    public ResponseEntity<HotelRs> deleteHotel(@Validated @RequestBody HotelRq hotelRq){
        return ResponseEntity.ok(hotelService.deleteHotel(hotelRq));
    }

    @PostMapping("/check-denied")
    public ResponseEntity<Boolean> checkDeniedUser(@RequestBody HotelRq hotelRq){
        return ResponseEntity.ok(hotelService.checkDeniedUser(hotelRq));
    }


}
