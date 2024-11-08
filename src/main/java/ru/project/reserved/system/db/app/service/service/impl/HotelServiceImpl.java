package ru.project.reserved.system.db.app.service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import ru.project.reserved.system.db.app.service.dto.HotelRequest;
import ru.project.reserved.system.db.app.service.dto.HotelResponse;
import ru.project.reserved.system.db.app.service.entity.Hotel;
import ru.project.reserved.system.db.app.service.mapper.HotelMapper;
import ru.project.reserved.system.db.app.service.repository.HotelRepository;
import ru.project.reserved.system.db.app.service.service.HotelService;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;

    @Override
    @SneakyThrows
    public List<HotelResponse> getAllHotels() {
        log.info("Get all hotels");
        return hotelMapper.mappingHotelListToHotelResponseList(hotelRepository.findAll());
    }

    @Override
    @SneakyThrows
    public List<HotelResponse> getAllHotelsByParam(HotelRequest hotelRequest) {
        log.info("Get all hotels by params");
        if (Strings.isNotBlank(hotelRequest.getName())) {
            log.info("Get all hotels by params name: {}", hotelRequest.getName());
            return hotelMapper.mappingHotelListToHotelResponseList(hotelRepository
                    .findHotelsByName(hotelRequest.getName()));
        }
        if(!hotelRequest.getCityList().isEmpty()){
            log.info("Get all hotels by params cityList: {}", hotelRequest.getCityList());
            return hotelMapper.mappingHotelListToHotelResponseList(hotelRepository
                    .findHotelsByCityList(hotelRequest.getCityList()));
        }
        if(Objects.nonNull(hotelRequest.getRating())){
            log.info("Get all hotels by params rating: {}", hotelRequest.getRating());
            return hotelMapper.mappingHotelListToHotelResponseList(hotelRepository
                    .findHotelsByRating(hotelRequest.getRating()));
        }
        if (Objects.nonNull(hotelRequest.getDistance())){
            log.info("Get all hotels by params distance: {}", hotelRequest.getDistance());
            List<Hotel> hotels = hotelRepository.findHotelsByDistance(hotelRequest.getDistance())
                    .stream()
                    .sorted(Comparator.comparing(Hotel::getDistance))
                    .toList();
            return hotelMapper.mappingHotelListToHotelResponseList(hotels);
        }
        return List.of(HotelResponse.builder()
                .errorMessage("Hotels not found!")
                .build());
    }

    @Override
    @SneakyThrows
    public HotelResponse createHotel(HotelRequest hotelRequest) {
        log.info("Create hotel");
        Hotel newHotel = hotelRepository.save(hotelMapper.mappingHotelRequestToHotel(hotelRequest));
        return hotelMapper.mappingHotelToHotelRequest(newHotel);
    }

    @Override
    public HotelResponse updateHotel(HotelRequest hotelRequest) {
        log.info("Update hotel");
        Optional<Hotel> hotel = hotelRepository.findById(hotelRequest.getId());
        if (hotel.isEmpty()) {
            return null;
        }
        hotelMapper.updateHotelByHotelRequest(hotel.get(), hotelRequest);
        Hotel updatedHotel = hotelRepository.save(hotel.get());
        return hotelMapper.mappingHotelToHotelRequest(updatedHotel);
    }

    @Override
    public void deleteHotel(HotelRequest hotelRequest) {
        log.info("Delete hotel");
        hotelRepository.deleteById(hotelRequest.getId());

    }
}
