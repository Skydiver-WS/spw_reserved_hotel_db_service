package ru.project.reserved.system.db.app.service.mapper;

import org.mapstruct.*;
import ru.project.reserved.system.db.app.service.dto.room.RoomRequest;
import ru.project.reserved.system.db.app.service.dto.room.RoomResponse;
import ru.project.reserved.system.db.app.service.entity.Booking;
import ru.project.reserved.system.db.app.service.entity.Hotel;
import ru.project.reserved.system.db.app.service.entity.Room;
import ru.project.reserved.system.db.app.service.exception.BookingException;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    @Mapping(source = "bookings", target = "bookingId", qualifiedByName = "setIdBooking",
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    RoomResponse roomToRoomResponse(Room room);

    List<RoomResponse> roomsToRoomResponses(List<Room> rooms);

    @Mapping(target = "numberApart", source = "roomRq.numberApart", qualifiedByName = "numberApart")
    @Mapping(target = "hotel", source = "hotel")
    @Mapping(target = "id", source = "roomRq.id")
    @Mapping(target = "description", source = "roomRq.description")
    Room roomResponseToRoom(RoomRequest roomRq, Hotel hotel);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRoom(@MappingTarget Room room, RoomRequest roomRequest);

    @Named("setIdBooking")
    default UUID setIdBooking(List<Booking> bookings) {
        if (bookings == null || bookings.isEmpty()) {
            return null;
        }
        return bookings.getFirst().getId();
    }

    @Named("numberApart")
    default Long setNumberApart(Long numberApart){
        if(Objects.isNull(numberApart)){
            throw new BookingException("Is parameter numberApart not be null");
        }
        return numberApart;
    }
}
