package ru.project.reserved.system.db.app.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import ru.project.reserved.system.db.app.service.dto.room.RoomRequest;
import ru.project.reserved.system.db.app.service.dto.room.RoomResponse;
import ru.project.reserved.system.db.app.service.entity.Booking;
import ru.project.reserved.system.db.app.service.entity.Room;

import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    @Mapping(source = "bookings", target = "bookingId", qualifiedByName = "setIdBooking")
    RoomResponse roomToRoomResponse(Room room);

    List<RoomResponse> roomsToRoomResponses(List<Room> rooms);

    Room roomResponseToRoom(RoomRequest room);
    void updateRoom(@MappingTarget Room room, RoomRequest roomRequest);

    @Named("setIdBooking")
    default UUID setIdBooking(List<Booking> bookings) {
        return bookings.getFirst().getId();
    }
}
