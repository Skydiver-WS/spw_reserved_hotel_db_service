package ru.project.reserved.system.db.app.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.project.reserved.system.db.app.service.dto.room.RoomRq;
import ru.project.reserved.system.db.app.service.entity.Booking;
import ru.project.reserved.system.db.app.service.entity.Room;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(target = "startReserved", source = "request.roomBooking.startReserved")
    @Mapping(target = "endReserved", source = "request.roomBooking.endReserved")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "room", source = "room")
    @Mapping(target = "created", expression = "java(new java.util.Date())")
    Booking bookingFromRoomRequest(RoomRq request, Room room);
}
