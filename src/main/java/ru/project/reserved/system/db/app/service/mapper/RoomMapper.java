package ru.project.reserved.system.db.app.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ru.project.reserved.system.db.app.service.dto.RoomRequest;
import ru.project.reserved.system.db.app.service.dto.RoomResponse;
import ru.project.reserved.system.db.app.service.entity.Room;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    RoomResponse roomToRoomResponse(Room room);

    List<RoomResponse> roomsToRoomResponses(List<Room> rooms);

    Room roomResponseToRoom(RoomRequest room);
    void updateRoom(@MappingTarget Room room, RoomRequest roomRequest);
}
