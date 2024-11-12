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
    public void getMessageGroupDataBase(String topic, String key, String message) {
        log.info("Topic: {}\n" +
                " Key: {}", topic, key);

        if(topic.equals(TopicType.CREATE_HOTEL.getTopic())){
            HotelRequest hotel = objectMapper.readValue(message, HotelRequest.class);
            HotelResponse hotelResponse = hotelService.createHotel(hotel);
            sendResponse(TopicType.HOTEL_RESPONSE.getTopic(), key, objectMapper.writeValueAsString(hotelResponse));
            return;
        }
        if(topic.equals(TopicType.UPDATE_HOTEL.getTopic())){
            HotelRequest hotel = objectMapper.readValue(message, HotelRequest.class);
            HotelResponse hotelResponse = hotelService.updateHotel(hotel);
            sendResponse(TopicType.HOTEL_RESPONSE.getTopic(), key, objectMapper.writeValueAsString(hotelResponse));
            return;
        }
        if(topic.equals(TopicType.REMOVE_HOTEL.getTopic())){
            HotelRequest hotel = objectMapper.readValue(message, HotelRequest.class);
            hotelService.deleteHotel(hotel);
            sendResponse(TopicType.HOTEL_RESPONSE.getTopic(), key, "Removed Hotel");
            return;
        }
        if (topic.equals(TopicType.FIND_ALL_HOTEL.getTopic())){
            log.info(message);
            List<HotelResponse> list = hotelService.getAllHotels();
            sendResponse(TopicType.HOTEL_RESPONSE.getTopic(), key, objectMapper.writeValueAsString(list));
            return;
        }
        if(topic.equals(TopicType.FIND_BY_PARAMETER_HOTEL.getTopic())){
            HotelRequest hotel = objectMapper.readValue(message, HotelRequest.class);
            List<HotelResponse> response = hotelService.getAllHotelsByParam(hotel);
            sendResponse(TopicType.HOTEL_RESPONSE.getTopic(), key, objectMapper.writeValueAsString(response));
            return;
        }


        if(topic.equals(TopicType.CREATE_ROOM.getTopic())){
            RoomRequest roomRequest = objectMapper.readValue(message, RoomRequest.class);
            RoomResponse roomResponse = roomService.createRoom(roomRequest);
            sendResponse(TopicType.ROOM_RESPONSE.getTopic(), key, objectMapper.writeValueAsString(roomResponse));
            return;
        }
        if(topic.equals(TopicType.UPDATE_ROOM.getTopic())){
            RoomRequest roomRequest = objectMapper.readValue(message, RoomRequest.class);
            RoomResponse roomResponse = roomService.updateRoom(roomRequest);
            sendResponse(TopicType.ROOM_RESPONSE.getTopic(), key, objectMapper.writeValueAsString(roomResponse));
            return;
        }
        if(topic.equals(TopicType.REMOVE_ROOM.getTopic())){
            RoomRequest roomRequest = objectMapper.readValue(message, RoomRequest.class);
            roomService.removeRoom(roomRequest.getId());
            sendResponse(TopicType.ROOM_RESPONSE.getTopic(), key, "Removed Room");
            return;
        }
        if(topic.equals(TopicType.FIND_BY_PARAMETER_ROOM.getTopic())){
            RoomRequest roomRequest = objectMapper.readValue(message, RoomRequest.class);
            List<RoomResponse> response = roomService.findRoomsForParameters(roomRequest);
            sendResponse(TopicType.ROOM_RESPONSE.getTopic(), key, objectMapper.writeValueAsString(response));
        }
    }


    private void sendResponse(String topic, String key, String response){
        kafkaTemplate.send(topic, key, response);
    }

}
