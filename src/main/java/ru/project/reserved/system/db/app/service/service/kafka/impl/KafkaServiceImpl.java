package ru.project.reserved.system.db.app.service.service.kafka.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
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
    private final KafkaTemplate<String, String> kafkaTemplate;


    @Override
    @SneakyThrows
    public void getMessageGroupDataBase(String key, String message) {

        if(key.equals(KeyType.CREATE_HOTEL.getKey())){
            HotelRequest hotel = objectMapper.readValue(message, HotelRequest.class);
            HotelResponse hotelResponse = hotelService.createHotel(hotel);
            sendResponse(TopicType.HOTEL_RESPONSE.getTopic(), objectMapper.writeValueAsString(hotelResponse));
            return;
        }
        if(key.equals(KeyType.UPDATE_HOTEL.getKey())){
            HotelRequest hotel = objectMapper.readValue(message, HotelRequest.class);
            HotelResponse hotelResponse = hotelService.updateHotel(hotel);
            sendResponse(TopicType.HOTEL_RESPONSE.getTopic(), objectMapper.writeValueAsString(hotelResponse));
            return;
        }
        if(key.equals(KeyType.REMOVE_HOTEL.getKey())){
            HotelRequest hotel = objectMapper.readValue(message, HotelRequest.class);
            hotelService.deleteHotel(hotel);
            sendResponse(TopicType.HOTEL_RESPONSE.getTopic(), "Removed Hotel");
            return;
        }
        if(key.equals(KeyType.READ_HOTEL.getKey())){
            HotelRequest hotel = objectMapper.readValue(message, HotelRequest.class);
            List<HotelResponse> response = hotelService.getAllHotelsByParam(hotel);
            sendResponse(TopicType.HOTEL_RESPONSE.getTopic(), objectMapper.writeValueAsString(response));
            return;
        }


        if(key.equals(KeyType.CREATE_ROOM.getKey())){
            RoomRequest roomRequest = objectMapper.readValue(message, RoomRequest.class);
            RoomResponse roomResponse = roomService.createRoom(roomRequest);
            sendResponse(TopicType.ROOM_RESPONSE.getTopic(), objectMapper.writeValueAsString(roomResponse));
            return;
        }
        if(key.equals(KeyType.UPDATE_ROOM.getKey())){
            RoomRequest roomRequest = objectMapper.readValue(message, RoomRequest.class);
            RoomResponse roomResponse = roomService.updateRoom(roomRequest);
            sendResponse(TopicType.ROOM_RESPONSE.getTopic(), objectMapper.writeValueAsString(roomResponse));
            return;
        }
        if(key.equals(KeyType.REMOVE_ROOM.getKey())){
            RoomRequest roomRequest = objectMapper.readValue(message, RoomRequest.class);
            roomService.removeRoom(roomRequest.getId());
            sendResponse(TopicType.ROOM_RESPONSE.getTopic(), "Removed Room");
            return;
        }
        if(key.equals(KeyType.READ_ROOM.getKey())){
            RoomRequest roomRequest = objectMapper.readValue(message, RoomRequest.class);
            List<RoomResponse> response = roomService.findRoomsForParameters(roomRequest);
            sendResponse(TopicType.ROOM_RESPONSE.getTopic(), objectMapper.writeValueAsString(response));
        }
    }


    private void sendResponse(String topic, String response){
        kafkaTemplate.send(topic, response);
    }

}
