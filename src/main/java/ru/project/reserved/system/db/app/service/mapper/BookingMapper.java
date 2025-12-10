package ru.project.reserved.system.db.app.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.project.reserved.system.db.app.service.dto.booking.BookingRs;
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


    @Mapping(target = "bookingId", source = "id")
    @Mapping(target = "hotelId", source = "room.hotel.id")
    @Mapping(target = "hotelCity", source = "room.hotel.address")
    @Mapping(target = "hotelName", source = "room.hotel.city")
    @Mapping(target = "roomId", source = "room.id")
    @Mapping(target = "coast", source = "booking", qualifiedByName = "calculateCoast")
    BookingRs bookingToBookingRs(Booking booking);

    @Named("calculateCoast")
    default Double calculateCoast(Booking booking){
        long diffInMillis =booking.getEndReserved().getTime() - booking.getStartReserved().getTime();
        int countDay  =  (int) (diffInMillis / (1000 * 60 * 60 * 24));
        return booking.getRoom().getCoast() * countDay;

    }
}
