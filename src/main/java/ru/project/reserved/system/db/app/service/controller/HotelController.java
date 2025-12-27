package ru.project.reserved.system.db.app.service.controller;

import io.micrometer.core.instrument.Counter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.project.reserved.system.db.app.service.dto.hotel.HotelRq;
import ru.project.reserved.system.db.app.service.dto.hotel.HotelRs;
import ru.project.reserved.system.db.app.service.service.HotelService;

@RestController
@RequestMapping("/api/v1/hotel")
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;
    private final Counter counter;

    @GetMapping
    public ResponseEntity<HotelRs> findAllHotels(){
        return ResponseEntity.ok(hotelService.getAllHotels());
    }

    @PostMapping("/search")
    public ResponseEntity<HotelRs> findHotels(@RequestBody HotelRq hotelRq){
        return ResponseEntity.ok(hotelService.getAllHotelsByParams(hotelRq));
    }

    @PostMapping
    public ResponseEntity<HotelRs> createHotel(@Validated @RequestBody HotelRq hotelRq){
        counter.increment();
        return ResponseEntity.ok(hotelService.createHotel(hotelRq));
    }

    @PutMapping
    public ResponseEntity<HotelRs> updateHotel(@Validated @RequestBody HotelRq hotelRq){
        return ResponseEntity.ok(hotelService.updateHotel(hotelRq));
    }

    @DeleteMapping
    public ResponseEntity<HotelRs> deleteHotel(@Validated @RequestBody HotelRq hotelRq){
        return ResponseEntity.ok(hotelService.deleteHotel(hotelRq));
    }


}
