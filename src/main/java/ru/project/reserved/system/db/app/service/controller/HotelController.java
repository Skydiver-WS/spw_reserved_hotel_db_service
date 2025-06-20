package ru.project.reserved.system.db.app.service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<HotelResponse>> findAllHotels(){
        return ResponseEntity.ok(hotelService.getAllHotels());
    }

    @PostMapping("/search")
    public ResponseEntity<List<HotelResponse>> findHotels(@RequestBody HotelRequest hotelRequest){
        return ResponseEntity.ok(hotelService.getAllHotelsByParams(hotelRequest));
    }

    @PostMapping
    public ResponseEntity<HotelResponse> createHotel(@RequestBody HotelRequest hotelRequest){
        return ResponseEntity.ok(hotelService.createHotel(hotelRequest));
    }

    @PutMapping
    public ResponseEntity<HotelResponse> updateHotel(@RequestBody HotelRequest hotelRequest){
        return ResponseEntity.ok(hotelService.updateHotel(hotelRequest));
    }

    @DeleteMapping
    public ResponseEntity<HotelResponse> deleteHotel(@RequestBody HotelRequest hotelRequest){
        return ResponseEntity.ok(hotelService.deleteHotel(hotelRequest));
    }


}
