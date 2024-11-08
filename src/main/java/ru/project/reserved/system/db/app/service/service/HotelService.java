package ru.project.reserved.system.db.app.service.service;

import ru.project.reserved.system.db.app.service.dto.HotelRequest;
import ru.project.reserved.system.db.app.service.dto.HotelResponse;

import java.util.List;

public interface HotelService {
    List<HotelResponse> getAllHotels();
    List<HotelResponse> getAllHotelsByParam(HotelRequest hotelRequest);
    HotelResponse createHotel(HotelRequest hotelRequest);
    HotelResponse updateHotel(HotelRequest hotelRequest);
    void deleteHotel(HotelRequest hotelRequest);

}
