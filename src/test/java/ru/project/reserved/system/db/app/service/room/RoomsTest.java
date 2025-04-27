package ru.project.reserved.system.db.app.service.room;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.project.reserved.system.db.app.service.AbstractTest;
import ru.project.reserved.system.db.app.service.dto.room.RoomRequest;
import ru.project.reserved.system.db.app.service.dto.room.RoomResponse;
import ru.project.reserved.system.db.app.service.dto.type.ClassRoomType;
import ru.project.reserved.system.db.app.service.entity.Hotel;
import ru.project.reserved.system.db.app.service.entity.Photo;
import ru.project.reserved.system.db.app.service.entity.Room;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class RoomsTest extends AbstractTest {

    @Test
    public void createRoomTest() {
        Hotel hotel = hotelRepository.findAll().getFirst();
        RoomRequest request = RoomRequest.builder().
                hotelId(hotel.getId())
                .numberApart(100L)
                .description("Test Description")
                .classRoomType(ClassRoomType.BUSINESS)
                .coast(100000.0)
                .photoList(new ArrayList<>(List.of(Photo.builder().photo("Test1").build(),
                        Photo.builder().photo("Test2").build(),
                        Photo.builder().photo("Test3").build())))
                .build();
        RoomResponse response = roomService.createRoom(request);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(ClassRoomType.BUSINESS, response.getClassRoomType());
    }

    @Test
    public void updateRoomTest() {
        List<Hotel> hotels = hotelRepository.findAll();
        Hotel hotel = null;
        Room room = null;
        for (Hotel hotel1 : hotels) {
            List<Room> rooms = roomRepository.findRoomsByHotel(hotel1);
            if (!rooms.isEmpty()) {
                hotel = hotel1;
                room = rooms.getFirst();
                break;
            }
        }

        RoomRequest roomRequest = RoomRequest.builder()
                .id(room.getId())
                .hotelId(hotel.getId())
                .coast(500.0)
                .classRoomType(ClassRoomType.STANDARD)
                .build();
        RoomResponse response = roomService.updateRoom(roomRequest);
        Assertions.assertNotNull(response);
        Assertions.assertEquals(ClassRoomType.STANDARD, response.getClassRoomType());
    }

    @Test
    public void deleteRoomTest() {
        List<Hotel> hotels = hotelRepository.findAll();
        Hotel hotel = null;
        Room room = null;
        for (Hotel hotel1 : hotels) {
            List<Room> rooms = roomRepository.findRoomsByHotel(hotel1);
            if (!rooms.isEmpty()) {
                hotel = hotel1;
                room = rooms.getFirst();
                break;
            }
        }
        roomService.removeRoom(hotel.getId(), room.getId());
        entityManager.clear();
        entityManager.flush();
        Optional<Room> roomOptional = roomRepository.findById(room.getId());
        Assertions.assertTrue(roomOptional.isEmpty());
    }

    @Test
    public void searchRoomTest() {
        Hotel hotel = hotelRepository.findHotelsByCity_Name("Тула").getFirst();
        RoomRequest request = RoomRequest.builder()
                .roomSearch(RoomRequest.RoomSearchRequest.builder()
                        .hotelId(hotel.getId())
                        .hotelName(hotel.getName())
                        .startReserved(new Date(17434656000000L))
                        .endReserved(new Date(17438112000000L))
                        .build())
                .build();
        List<RoomResponse> response = roomService.findRoomsForParameters(request);
        Assertions.assertFalse(response.isEmpty());
    }

    @Test
    public void searchNoRoomTest() {
        Hotel hotel = hotelRepository.findHotelsByCity_Name("Тула").getFirst();
        RoomRequest request = RoomRequest.builder()
                .roomSearch(RoomRequest.RoomSearchRequest.builder()
                        .hotelId(hotel.getId())
                        .hotelName(hotel.getName())
                        .startReserved(new Date(1743465600000L))
                        .endReserved(new Date(1743811200000L))
                        .build())
                .build();
        List<RoomResponse> response = roomService.findRoomsForParameters(request);
        Assertions.assertTrue(response.isEmpty());
    }

}
