package ru.project.reserved.system.db.app.service.service;

import ru.project.reserved.system.db.app.service.dto.hotel.HotelRequest;
import ru.project.reserved.system.db.app.service.dto.hotel.HotelResponse;

import java.util.List;

public interface HotelSearchService {

    List<HotelResponse> searchHotels(HotelRequest request);
}
