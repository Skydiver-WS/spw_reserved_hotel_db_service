package ru.project.reserved.system.db.app.service.mapper;

import org.mapstruct.*;
import ru.project.reserved.system.db.app.service.dto.hotel.HotelRequest;
import ru.project.reserved.system.db.app.service.dto.hotel.HotelResponse;
import ru.project.reserved.system.db.app.service.entity.City;
import ru.project.reserved.system.db.app.service.entity.Hotel;
import ru.project.reserved.system.db.app.service.entity.Room;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", uses = {RoomMapper.class, CityMapper.class})
public interface HotelMapper {

    @Mapping(target = "countApart", expression = "java(0)")
    @Mapping(target = "cityList", source = "city.name", qualifiedByName = "setCities")
    Hotel mappingHotelRequestToHotel(HotelRequest hotelRequest);

    List<HotelResponse> mappingHotelListToHotelResponseList(List<Hotel> hotelList);

    @Mapping(target = "minCoast", source = "roomList", qualifiedByName = "minCoast")
    HotelResponse mappingHotelToHotelRequest(Hotel hotel);

    HotelResponse mappingResponseMappingFromHotelRequest(HotelRequest hotelRequest);

    List<HotelResponse> mappingHotelResponseToHotelRequestList(List<HotelRequest> hotelList);

    @Mapping(target = "name", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "description", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "address", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "distance", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "rating", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateHotelByHotelRequest(@MappingTarget Hotel hotel, HotelRequest hotelRequest);

    @Named("minCoast")
    default Double minCoast(List<Room> roomList) {
        return roomList == null || roomList.isEmpty()
                ? 0.0
                : roomList.stream()
                .min(Comparator.comparingDouble(Room::getCoast))
                .map(Room::getCoast)
                .orElse(0.0);
    }

    @Named("setCities")
    default Set<City> setCities(String city) {
        return Set.of(City.builder()
                .name(city)
                .build());
    }
}
