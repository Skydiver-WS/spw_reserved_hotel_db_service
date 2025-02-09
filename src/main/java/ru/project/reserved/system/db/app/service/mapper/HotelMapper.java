package ru.project.reserved.system.db.app.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import ru.project.reserved.system.db.app.service.dto.city.CityRequest;
import ru.project.reserved.system.db.app.service.dto.hotel.HotelRequest;
import ru.project.reserved.system.db.app.service.dto.hotel.HotelResponse;
import ru.project.reserved.system.db.app.service.entity.Address;
import ru.project.reserved.system.db.app.service.entity.City;
import ru.project.reserved.system.db.app.service.entity.Hotel;
import ru.project.reserved.system.db.app.service.entity.Room;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", uses = {RoomMapper.class, CityMapper.class, AddressMapper.class})
public interface HotelMapper {

    @Mapping(target = "countApart", expression = "java(0)")
    @Mapping(target = "freeApart", expression = "java(0)")
    @Mapping(target = "cityList", source = "city.name", qualifiedByName = "setCities")
    @Mapping(target = "addressList", source = "city", qualifiedByName = "setAddress")
    Hotel mappingHotelRequestToHotel(HotelRequest hotelRequest);

    List<HotelResponse> mappingHotelListToHotelResponseList(List<Hotel> hotelList);

    @Mapping(target = "freeApart", source = "roomList", qualifiedByName = "countFreeApart")
    @Mapping(target = "minCoast", source = "roomList", qualifiedByName = "minCoast")
    HotelResponse mappingHotelToHotelRequest(Hotel hotel);

    HotelResponse mappingResponseMappingFromHotelRequest(HotelRequest hotelRequest);

    List<HotelResponse> mappingHotelResponseToHotelRequestList(List<HotelRequest> hotelList);

    void updateHotelByHotelRequest(@MappingTarget Hotel hotel, HotelRequest hotelRequest);

    @Named("countFreeApart")
    default Integer countFreeApart(List<Room> roomList) {
        return roomList == null ? 0 : roomList.size();
    }

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

    @Named("setAddress")
    default List<Address> setAddress(CityRequest request) {
        return List.of(request.getAddress());
    }
}
