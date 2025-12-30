package ru.project.reserved.system.db.app.service.service;

import org.springframework.data.domain.Pageable;
import ru.project.reserved.system.db.app.service.dto.hotel.HotelRq;
import ru.project.reserved.system.db.app.service.dto.hotel.HotelRs;

public interface HotelService {
    HotelRs getAllHotels(Pageable pageable);
    HotelRs getAllHotelsByParams(HotelRq searchRequestRequest);
    HotelRs createHotel(HotelRq hotelRq);
    HotelRs updateHotel(HotelRq hotelRq);
    HotelRs deleteHotel(HotelRq hotelRq);
    boolean checkDeniedUser(HotelRq hotelRq);

}
