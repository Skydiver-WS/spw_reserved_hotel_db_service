package ru.project.reserved.system.db.app.service.service;

import ru.project.reserved.system.db.app.service.dto.hotel.HotelRequest;
import ru.project.reserved.system.db.app.service.dto.hotel.HotelResponse;

import java.util.List;

public interface HotelService {
    HotelResponse getAllHotels();
    HotelResponse getAllHotelsByParams(HotelRequest searchRequestRequest);
    HotelResponse createHotel(HotelRequest hotelRequest);
    HotelResponse updateHotel(HotelRequest hotelRequest);
    HotelResponse deleteHotel(HotelRequest hotelRequest);

}
