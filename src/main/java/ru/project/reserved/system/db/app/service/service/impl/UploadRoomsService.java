package ru.project.reserved.system.db.app.service.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import ru.project.reserved.system.db.app.service.dto.room.RoomRequest;
import ru.project.reserved.system.db.app.service.entity.Hotel;
import ru.project.reserved.system.db.app.service.entity.Room;
import ru.project.reserved.system.db.app.service.mapper.RoomMapper;
import ru.project.reserved.system.db.app.service.repository.HotelRepository;
import ru.project.reserved.system.db.app.service.repository.RoomRepository;
import ru.project.reserved.system.db.app.service.service.kafka.KafkaService;

import java.util.*;
import java.util.concurrent.CountDownLatch;

@Service
@RequiredArgsConstructor
@Slf4j
@Profile({"test"})
public class UploadRoomsService {
    private final HotelRepository hotelRepository;
    private final SaveAllService saveAllService;
    private final ObjectMapper objectMapper;
    private final RoomMapper roomMapper;
    private static final CountDownLatch latch = new CountDownLatch(93502);
    private static final List<Room> rooms = new ArrayList<>();
    private static final Map<Long, Hotel> hotels = new HashMap<>();

    @SneakyThrows
    public void getMessageGroupDataBase(String topic, String key, String message) {
        RoomRequest roomRequest = new RoomRequest();
        if (Strings.isNotBlank(message)) {
            roomRequest = objectMapper.readValue(message, RoomRequest.class);
        }
        Room room = roomMapper.roomResponseToRoom(roomRequest);
        Optional<Hotel> hotel;
        Long hotelId = roomRequest.getHotelId();
        if (hotels.containsKey(hotelId)) {
            hotel = Optional.of(hotels.get(hotelId));
        } else {
            hotel = hotelRepository.findById(hotelId);
        }
        if (hotel.isPresent()) {
            room.setHotel(hotel.get());
            hotels.put(hotelId, hotel.get());
            rooms.add(room);
        }
        latch.countDown();
        log.info("Count rooms: {}. Count message in kafka {}", rooms.size(), latch.getCount());


        if (latch.getCount() == 0) {
            log.info("Saving rooms");
            upload();
        }

    }

    private void upload() {
        List<Room> lists = new ArrayList<>();
        int i = 0;
        while (true) {
            try {
                lists.add(rooms.get(i));
                if (lists.size() == 1000) {
                    log.info("Uploading rooms");
                    saveAllService.saveAll(lists);
                    for (int j = 0; j < lists.size(); j++) {
                        rooms.remove(i);
                    }

                    i=0;
                    lists.clear();
                }
            } catch (Exception e) {
                e.getMessage();
            }
            if (rooms.size() < i) {
                lists.addAll(rooms);
                saveAllService.saveAll(lists);
                break;
            }
            i++;
        }
        log.info("Rooms saved successful");
    }



}
