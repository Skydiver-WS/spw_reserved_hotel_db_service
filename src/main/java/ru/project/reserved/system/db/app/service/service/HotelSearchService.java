package ru.project.reserved.system.db.app.service.service;

import org.springframework.data.domain.Pageable;
import ru.project.reserved.system.db.app.service.dto.hotel.HotelRq;
import ru.project.reserved.system.db.app.service.dto.hotel.HotelRs;

import java.util.List;

public interface HotelSearchService {

    List<HotelRs> searchHotels(HotelRq request, Pageable pageable);
}
