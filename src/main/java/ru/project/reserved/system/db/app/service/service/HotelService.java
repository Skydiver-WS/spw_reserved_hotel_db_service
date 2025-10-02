package ru.project.reserved.system.db.app.service.service;

import ru.project.reserved.system.db.app.service.dto.hotel.HotelRq;
import ru.project.reserved.system.db.app.service.dto.hotel.HotelRs;

public interface HotelService {
    HotelRs getAllHotels();
    HotelRs getAllHotelsByParams(HotelRq searchRequestRequest);
    HotelRs createHotel(HotelRq hotelRq);
    HotelRs updateHotel(HotelRq hotelRq);
    HotelRs deleteHotel(HotelRq hotelRq);

}
