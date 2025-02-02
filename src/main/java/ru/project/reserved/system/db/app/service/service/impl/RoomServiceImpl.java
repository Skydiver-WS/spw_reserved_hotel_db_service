package ru.project.reserved.system.db.app.service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.project.reserved.system.db.app.service.dto.room.RoomRequest;
import ru.project.reserved.system.db.app.service.dto.room.RoomResponse;
import ru.project.reserved.system.db.app.service.entity.Room;
import ru.project.reserved.system.db.app.service.mapper.RoomMapper;
import ru.project.reserved.system.db.app.service.repository.RoomRepository;
import ru.project.reserved.system.db.app.service.service.RoomSearchService;
import ru.project.reserved.system.db.app.service.service.RoomService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;
    private final RoomSearchService searchService;


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
    public List<RoomResponse> findRoomsForParameters(RoomRequest request) {
        log.info("Find rooms for parameters");
        List<RoomResponse> roomResponses = searchService.searchRoomByParameter(request);
        return roomResponses.isEmpty() ? List.of(RoomResponse.builder()
                .errorMessage("Rooms not found")
                .build()) : roomResponses;
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
