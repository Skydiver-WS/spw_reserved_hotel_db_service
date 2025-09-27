package ru.project.reserved.system.db.app.service.mapper;

import org.mapstruct.*;
import ru.project.reserved.system.db.app.service.dto.room.RoomRequest;
import ru.project.reserved.system.db.app.service.dto.room.RoomResponse;
import ru.project.reserved.system.db.app.service.entity.Booking;
import ru.project.reserved.system.db.app.service.entity.Room;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    @Mapping(source = "bookings", target = "bookingId", qualifiedByName = "setIdBooking",
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    RoomResponse roomToRoomResponse(Room room);

    List<RoomResponse> roomsToRoomResponses(List<Room> rooms);

    Room roomResponseToRoom(RoomRequest room);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateRoom(@MappingTarget Room room, RoomRequest roomRequest);

    @Named("setIdBooking")
    default UUID setIdBooking(List<Booking> bookings) {
        if (bookings == null || bookings.isEmpty()) {
            return null;
        }
        return bookings.getFirst().getId();
    }
}
