package ru.project.reserved.system.db.app.service.mapper;

import org.mapstruct.*;
import ru.project.reserved.system.db.app.service.dto.hotel.HotelRq;
import ru.project.reserved.system.db.app.service.dto.hotel.HotelRs;
import ru.project.reserved.system.db.app.service.entity.Hotel;
import ru.project.reserved.system.db.app.service.entity.Room;

import java.util.Comparator;
import java.util.List;

@Mapper(componentModel = "spring", uses = {RoomMapper.class, PhotoMapper.class })
public interface HotelMapper {

    @Mapping(target = "countApart", expression = "java(0)")
    @Mapping(target = "users", source = "usersRq", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Hotel mappingHotelRequestToHotel(HotelRq hotelRq);

    List<HotelRs> mappingHotelListToHotelResponseList(List<Hotel> hotelList);

    @Mapping(target = "minCoast", source = "roomList", qualifiedByName = "minCoast")
    HotelRs mappingHotelToHotelRequest(Hotel hotel);

    HotelRs mappingResponseMappingFromHotelRequest(HotelRq hotelRq);

    List<HotelRs> mappingHotelResponseToHotelRequestList(List<HotelRq> hotelList);

    @Mapping(target = "name", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "description", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "address", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "distance", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "rating", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "users", source = "usersRq", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "photos", ignore = true)
    void updateHotelByHotelRequest(@MappingTarget Hotel hotel, HotelRq hotelRq);

    @Named("minCoast")
    default Double minCoast(List<Room> roomList) {
        return roomList == null || roomList.isEmpty()
                ? 0.0
                : roomList.stream()
                .min(Comparator.comparingDouble(Room::getCoast))
                .map(Room::getCoast)
                .orElse(0.0);
    }

    @AfterMapping
    default void sortHotelByRating(@MappingTarget List<HotelRs> responses) {
        if (responses != null) {
            responses.sort(Comparator.comparingDouble(HotelRs::getRating).reversed());
        }
    }
}

