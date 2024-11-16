package ru.project.reserved.system.db.app.service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.project.reserved.system.db.app.service.dto.RoomRequest;
import ru.project.reserved.system.db.app.service.dto.RoomResponse;
import ru.project.reserved.system.db.app.service.entity.Room;
import ru.project.reserved.system.db.app.service.mapper.RoomMapper;
import ru.project.reserved.system.db.app.service.repository.RoomRepository;
import ru.project.reserved.system.db.app.service.service.RoomService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;


    @Override
    public List<RoomResponse> findAllRooms() {
        log.info("Find all rooms");
        return roomMapper.roomsToRoomResponses(roomRepository.findAll());
    }

    @Override
    public RoomResponse createRoom(RoomRequest room) {
        log.info("Create room");
        Room  newRoom = roomRepository.save(roomMapper.roomResponseToRoom(room));
        return roomMapper.roomToRoomResponse(newRoom);
    }

    @Override
    public List<RoomResponse> findRoomsForParameters(RoomRequest room) {
        log.info("Find rooms for parameters");
        if(Objects.nonNull(room.getClassRoomType())){
            return roomMapper.roomsToRoomResponses(roomRepository.findRoomsByClassRoomType(room.getClassRoomType()));
        }
        if (Objects.nonNull(room.getStatus())){
            return roomMapper.roomsToRoomResponses(roomRepository.findRoomsByStatus(room.getStatus()));
        }
        return List.of(RoomResponse.builder()
                .errorMessage("Room not found")
                .build());
    }

    @Override
    public RoomResponse updateRoom(RoomRequest roomRequest) {
        Optional<Room> findRoom = roomRepository.findById(roomRequest.getId());
        if(findRoom.isPresent()) {
            log.info("Update room");
            roomMapper.updateRoom(findRoom.get(), roomRequest);
            Room newRoom = roomRepository.save(findRoom.get());
            return roomMapper.roomToRoomResponse(newRoom);
        }
        return RoomResponse.builder()
                .errorMessage("Room not found")
                .build();
    }

    @Override
    public void removeRoom(Integer id) {
        log.info("Remove room");
        roomRepository.deleteById(id);
    }
}
