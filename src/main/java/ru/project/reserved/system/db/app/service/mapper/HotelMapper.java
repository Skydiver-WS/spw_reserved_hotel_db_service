package ru.project.reserved.system.db.app.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ru.project.reserved.system.db.app.service.dto.HotelRequest;
import ru.project.reserved.system.db.app.service.dto.HotelResponse;
import ru.project.reserved.system.db.app.service.entity.Hotel;

import java.util.List;

@Mapper(componentModel = "spring", uses = {RoomMapper.class, CityMapper.class})
public interface HotelMapper {

    Hotel mappingHotelRequestToHotel(HotelRequest hotelRequest);

    List<HotelResponse> mappingHotelListToHotelResponseList(List<Hotel> hotelList);

    HotelResponse mappingHotelToHotelRequest(Hotel hotel);

    void updateHotelByHotelRequest(@MappingTarget Hotel hotel, HotelRequest hotelRequest);
}
