package ru.project.reserved.system.db.app.service.service.kafka.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.project.reserved.system.db.app.service.dto.*;
import ru.project.reserved.system.db.app.service.service.HotelService;
import ru.project.reserved.system.db.app.service.service.RoomService;
import ru.project.reserved.system.db.app.service.service.kafka.KafkaService;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaServiceImpl implements KafkaService {

    private final ObjectMapper objectMapper;
    private final HotelService hotelService;
    private final RoomService roomService;


    @Override
    @SneakyThrows
    public String getMessageGroupDataBase(String key, String message) {

        if(key.equals(KeyType.CREATE_HOTEL.getKey())){
            HotelRequest hotel = objectMapper.convertValue(message, HotelRequest.class);
            HotelResponse hotelResponse = hotelService.createHotel(hotel);
            return objectMapper.writeValueAsString(hotelResponse);
        }
        if(key.equals(KeyType.UPDATE_HOTEL.getKey())){
            HotelRequest hotel = objectMapper.convertValue(message, HotelRequest.class);
            HotelResponse hotelResponse = hotelService.updateHotel(hotel);
            return objectMapper.writeValueAsString(hotelResponse);
        }
        if(key.equals(KeyType.REMOVE_HOTEL.getKey())){
            HotelRequest hotel = objectMapper.convertValue(message, HotelRequest.class);
            hotelService.deleteHotel(hotel);
            return null;
        }
        if(key.equals(KeyType.READ_HOTEL.getKey())){
            HotelRequest hotel = objectMapper.convertValue(message, HotelRequest.class);
            List<HotelResponse> response = hotelService.getAllHotelsByParam(hotel);
            return objectMapper.writeValueAsString(response);
        }


        if(key.equals(KeyType.CREATE_ROOM.getKey())){
            RoomRequest roomRequest = objectMapper.convertValue(message, RoomRequest.class);
            RoomResponse roomResponse = roomService.createRoom(roomRequest);
            return objectMapper.writeValueAsString(roomResponse);
        }
        if(key.equals(KeyType.UPDATE_ROOM.getKey())){
            RoomRequest roomRequest = objectMapper.convertValue(message, RoomRequest.class);
            RoomResponse roomResponse = roomService.updateRoom(roomRequest);
            return objectMapper.writeValueAsString(roomResponse);
        }
        if(key.equals(KeyType.REMOVE_ROOM.getKey())){
            RoomRequest roomRequest = objectMapper.convertValue(message, RoomRequest.class);
            roomService.removeRoom(roomRequest.getId());
            return null;
        }
        if(key.equals(KeyType.READ_ROOM.getKey())){
            RoomRequest roomRequest = objectMapper.convertValue(message, RoomRequest.class);
            List<RoomResponse> response = roomService.findRoomsForParameters(roomRequest);
            return objectMapper.writeValueAsString(response);
        }

        return "";
    }

}
