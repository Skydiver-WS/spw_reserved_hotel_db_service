package ru.project.reserved.system.db.app.service.hotel;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import ru.project.reserved.system.db.app.service.AbstractTest;
import ru.project.reserved.system.db.app.service.dto.hotel.HotelRequest;
import ru.project.reserved.system.db.app.service.dto.hotel.HotelResponse;
import ru.project.reserved.system.db.app.service.entity.Hotel;

import java.io.File;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

public class HotelsTest extends AbstractTest {

    @Test
    @SneakyThrows
    @Order(1)
    public void createHotelTest() {
        String jsonHotel = new String(Files.readAllBytes(new File("src/test/resources/hotel/hotelTest.json").toPath()));
        HotelRequest hotelRequest = objectMapper.readValue(jsonHotel, HotelRequest.class);
        HotelResponse response = hotelService.createHotel(hotelRequest);
        Assertions.assertNotNull(response);
        Assertions.assertEquals("Hotel Рамен", response.getName());
        Assertions.assertEquals("123 Beach Avenue", response.getAddress());
    }

    @Test
    @Order(2)
    public void updateHotelTest() {
        Hotel hotel = hotelRepository.findAll().getFirst();
        HotelRequest hotelRequest = HotelRequest.builder()
                .id(hotel.getId())
                .name("Hotel Atas")
                .build();
        HotelResponse response = hotelService.updateHotel(hotelRequest);
        Assertions.assertNotNull(response);
        Assertions.assertEquals("Hotel Atas", response.getName());
    }

    @Test
    @Order(3)
    public void deleteHotelTest() {
        Hotel hotel = hotelRepository.findAll().getFirst();
        HotelRequest hotelRequest = HotelRequest.builder()
                .id(hotel.getId())
                .build();
        HotelResponse response = hotelService.deleteHotel(hotelRequest);
        Assertions.assertEquals("Hotel with id "  + hotel.getId()  + " delete", response.getMessage());
    }

    @Test
    @Order(4)
    public void searchHotelByBookingDateRoomsTest() {
        HotelRequest request = HotelRequest.builder()
                .hotelSearch(HotelRequest.HotelSearchRequest.builder()
                        .city("Тула")
                        .startReserved(new Date(LocalDateTime.now()
                                .plusDays(10)
                                .atZone(ZoneId.systemDefault()) // Учитываем часовой пояс
                                .toInstant()
                                .toEpochMilli()))
                        .endReserved(new Date(LocalDateTime.now()
                                .plusDays(15)
                                .atZone(ZoneId.systemDefault()) // Учитываем часовой пояс
                                .toInstant()
                                .toEpochMilli()))
                        .build())
                .build();
        HotelResponse response = hotelService.getAllHotelsByParams(request);
        Assertions.assertNotNull(response);
        Assertions.assertEquals("Hotel Тула", response.getHotels().getFirst().getName());
    }
    @Test
    @Order(5)
    public void searchHotelByBookingDateNoRoomsTest() {
        HotelRequest request = HotelRequest.builder()
                .hotelSearch(HotelRequest.HotelSearchRequest.builder()
                        .city("Тула")
                        .startReserved(new Date())
                        .endReserved(new Date(LocalDateTime.now()
                                .plusDays(5)
                                .atZone(ZoneId.systemDefault()) // Учитываем часовой пояс
                                .toInstant()
                                .toEpochMilli()))
                        .build())
                .build();
        HotelResponse response = hotelService.getAllHotelsByParams(request);;
        Assertions.assertEquals(0, response.getHotels().size());
    }

    @Test
    @Order(6)
    public void searchHotelByRatingTest(){
        HotelRequest request = HotelRequest.builder()
                .hotelSearch(HotelRequest.HotelSearchRequest.builder()
                        .city("Тула")
                        .startReserved(new Date(LocalDateTime.now()
                                .plusDays(10)
                                .atZone(ZoneId.systemDefault()) // Учитываем часовой пояс
                                .toInstant()
                                .toEpochMilli()))
                        .endReserved(new Date(LocalDateTime.now()
                                .plusDays(15)
                                .atZone(ZoneId.systemDefault()) // Учитываем часовой пояс
                                .toInstant()
                                .toEpochMilli()))
                        .rating(2.0)
                        .build())
                .build();
        HotelResponse response = hotelService.getAllHotelsByParams(request);
        Assertions.assertNotNull(response);
        Assertions.assertEquals("Hotel Тула", response.getHotels().getFirst().getName());
    }

    @Test
    @Order(7)
    public void searchHotelByRatingNoHotelTest(){
        HotelRequest request = HotelRequest.builder()
                .hotelSearch(HotelRequest.HotelSearchRequest.builder()
                        .city("Тула")
                        .startReserved(new Date(LocalDateTime.now()
                                .plusDays(10)
                                .atZone(ZoneId.systemDefault()) // Учитываем часовой пояс
                                .toInstant()
                                .toEpochMilli()))
                        .endReserved(new Date(LocalDateTime.now()
                                .plusDays(15)
                                .atZone(ZoneId.systemDefault()) // Учитываем часовой пояс
                                .toInstant()
                                .toEpochMilli()))
                        .rating(7.0)
                        .build())
                .build();
        HotelResponse response = hotelService.getAllHotelsByParams(request);
        Assertions.assertEquals(0, response.getHotels().size());
    }

    @Test
    @Order(8)
    public void searchHotelByDistanceTest(){
        HotelRequest request = HotelRequest.builder()
                .hotelSearch(HotelRequest.HotelSearchRequest.builder()
                        .city("Тула")
                        .startReserved(new Date(LocalDateTime.now()
                                .plusDays(10)
                                .atZone(ZoneId.systemDefault()) // Учитываем часовой пояс
                                .toInstant()
                                .toEpochMilli()))
                        .endReserved(new Date(LocalDateTime.now()
                                .plusDays(15)
                                .atZone(ZoneId.systemDefault()) // Учитываем часовой пояс
                                .toInstant()
                                .toEpochMilli()))
                        .distance(20.0)
                        .build())
                .build();
        HotelResponse response = hotelService.getAllHotelsByParams(request);
        Assertions.assertNotNull(response);
        Assertions.assertEquals("Hotel Тула", response.getHotels().getFirst().getName());
    }

    @Test
    @Order(9)
    public void searchHotelByDistanceNoHotelTest(){
        HotelRequest request = HotelRequest.builder()
                .hotelSearch(HotelRequest.HotelSearchRequest.builder()
                        .city("Тула")
                        .startReserved(new Date(LocalDateTime.now()
                                .plusDays(10)
                                .atZone(ZoneId.systemDefault()) // Учитываем часовой пояс
                                .toInstant()
                                .toEpochMilli()))
                        .endReserved(new Date(LocalDateTime.now()
                                .plusDays(15)
                                .atZone(ZoneId.systemDefault()) // Учитываем часовой пояс
                                .toInstant()
                                .toEpochMilli()))
                        .distance(10.0)
                        .build())
                .build();
        HotelResponse response = hotelService.getAllHotelsByParams(request);
        Assertions.assertEquals(0, response.getHotels().size());
    }

    @Test
    @Order(10)
    public void searchHotelByCoastTest(){
        HotelRequest request = HotelRequest.builder()
                .hotelSearch(HotelRequest.HotelSearchRequest.builder()
                        .city("Тула")
                        .startReserved(new Date(LocalDateTime.now()
                                .plusDays(10)
                                .atZone(ZoneId.systemDefault()) // Учитываем часовой пояс
                                .toInstant()
                                .toEpochMilli()))
                        .endReserved(new Date(LocalDateTime.now()
                                .plusDays(15)
                                .atZone(ZoneId.systemDefault()) // Учитываем часовой пояс
                                .toInstant()
                                .toEpochMilli()))
                        .coastMin(2000L)
                        .coastMax(10000L)
                        .build())
                .build();
        HotelResponse response = hotelService.getAllHotelsByParams(request);
        Assertions.assertNotNull(response);
        Assertions.assertEquals("Hotel Тула", response.getHotels().getFirst().getName());
    }

    @Test
    @Order(11)
    public void searchHotelByCoastNoHotelTest(){
        HotelRequest request = HotelRequest.builder()
                .hotelSearch(HotelRequest.HotelSearchRequest.builder()
                        .city("Тула")
                        .startReserved(new Date(1743465600000L))
                        .endReserved(new Date(1743811200000L))
                        .coastMin(200000L)
                        .coastMax(1000000L)
                        .build())
                .build();
        HotelResponse response = hotelService.getAllHotelsByParams(request);
        Assertions.assertEquals(0, response.getHotels().size());
    }

    @Test
    @Order(12)
    public void searchHotelTest(){
        HotelRequest request = HotelRequest.builder()
                .hotelSearch(HotelRequest.HotelSearchRequest.builder()
                        .city("Тула")
                        .startReserved(new Date(LocalDateTime.now()
                                .plusDays(10)
                                .atZone(ZoneId.systemDefault()) // Учитываем часовой пояс
                                .toInstant()
                                .toEpochMilli()))
                        .endReserved(new Date(LocalDateTime.now()
                                .plusDays(15)
                                .atZone(ZoneId.systemDefault()) // Учитываем часовой пояс
                                .toInstant()
                                .toEpochMilli()))
                        .rating(2.0)
                        .distance(20.0)
                        .coastMin(2000L)
                        .coastMax(10000L)
                        .build())
                .build();
        HotelResponse response = hotelService.getAllHotelsByParams(request);
        Assertions.assertNotNull(response);
        Assertions.assertEquals("Hotel Тула", response.getHotels().getFirst().getName());
    }

    @Test
    @Order(13)
    public void searchHotelNoHotelTest(){
        HotelRequest request = HotelRequest.builder()
                .hotelSearch(HotelRequest.HotelSearchRequest.builder()
                        .city("Тула")
                        .startReserved(new Date())
                        .endReserved(new Date(LocalDateTime.now()
                                .plusDays(5)
                                .atZone(ZoneId.systemDefault()) // Учитываем часовой пояс
                                .toInstant()
                                .toEpochMilli()))
                        .rating(5.0)
                        .distance(10.0)
                        .coastMin(20000L)
                        .coastMax(100000L)
                        .build())
                .build();
        HotelResponse response = hotelService.getAllHotelsByParams(request);
        Assertions.assertEquals(0, response.getHotels().size());
    }

}
