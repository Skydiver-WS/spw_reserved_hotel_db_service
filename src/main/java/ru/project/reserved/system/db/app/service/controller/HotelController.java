package ru.project.reserved.system.db.app.service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.project.reserved.system.db.app.service.dto.hotel.HotelRequest;
import ru.project.reserved.system.db.app.service.dto.hotel.HotelResponse;
import ru.project.reserved.system.db.app.service.service.HotelService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/hotel")
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;

    @GetMapping
    public ResponseEntity<HotelResponse> findAllHotels(){
        return ResponseEntity.ok(hotelService.getAllHotels());
    }

    @PostMapping("/search")
    public ResponseEntity<HotelResponse> findHotels(@RequestBody HotelRequest hotelRequest){
        return ResponseEntity.ok(hotelService.getAllHotelsByParams(hotelRequest));
    }

    @PostMapping
    public ResponseEntity<HotelResponse> createHotel(@Validated @RequestBody HotelRequest hotelRequest){
        return ResponseEntity.ok(hotelService.createHotel(hotelRequest));
    }

    @PutMapping
    public ResponseEntity<HotelResponse> updateHotel(@Validated @RequestBody HotelRequest hotelRequest){
        return ResponseEntity.ok(hotelService.updateHotel(hotelRequest));
    }

    @DeleteMapping
    public ResponseEntity<HotelResponse> deleteHotel(@Validated @RequestBody HotelRequest hotelRequest){
        return ResponseEntity.ok(hotelService.deleteHotel(hotelRequest));
    }


}
