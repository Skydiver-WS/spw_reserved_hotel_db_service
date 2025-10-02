package ru.project.reserved.system.db.app.service.hotel;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import ru.project.reserved.system.db.app.service.AbstractTest;
import ru.project.reserved.system.db.app.service.dto.hotel.HotelRq;
import ru.project.reserved.system.db.app.service.dto.hotel.HotelRs;
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
        HotelRq hotelRq = objectMapper.readValue(jsonHotel, HotelRq.class);
        HotelRs response = hotelService.createHotel(hotelRq);
        Assertions.assertNotNull(response);
        Assertions.assertEquals("Hotel Рамен", response.getName());
        Assertions.assertEquals("123 Beach Avenue", response.getAddress());
    }

    @Test
    @Order(2)
    public void updateHotelTest() {
        Hotel hotel = hotelRepository.findAll().getFirst();
        HotelRq hotelRq = HotelRq.builder()
                .id(hotel.getId())
                .name("Hotel Atas")
                .build();
        HotelRs response = hotelService.updateHotel(hotelRq);
        Assertions.assertNotNull(response);
        Assertions.assertEquals("Hotel Atas", response.getName());
    }

    @Test
    @Order(3)
    public void deleteHotelTest() {
        Hotel hotel = hotelRepository.findAll().getFirst();
        HotelRq hotelRq = HotelRq.builder()
                .id(hotel.getId())
                .build();
        HotelRs response = hotelService.deleteHotel(hotelRq);
        Assertions.assertEquals("Hotel with id "  + hotel.getId()  + " delete", response.getMessage());
    }

    @Test
    @Order(4)
    public void searchHotelByBookingDateRoomsTest() {
        HotelRq request = HotelRq.builder()
                .hotelSearch(HotelRq.HotelSearchRequest.builder()
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
        HotelRs response = hotelService.getAllHotelsByParams(request);
        Assertions.assertNotNull(response);
        Assertions.assertEquals("Hotel Тула", response.getHotels().getFirst().getName());
    }
    @Test
    @Order(5)
    public void searchHotelByBookingDateNoRoomsTest() {
        HotelRq request = HotelRq.builder()
                .hotelSearch(HotelRq.HotelSearchRequest.builder()
                        .city("Тула")
                        .startReserved(new Date())
                        .endReserved(new Date(LocalDateTime.now()
                                .plusDays(5)
                                .atZone(ZoneId.systemDefault()) // Учитываем часовой пояс
                                .toInstant()
                                .toEpochMilli()))
                        .build())
                .build();
        HotelRs response = hotelService.getAllHotelsByParams(request);;
        Assertions.assertEquals(0, response.getHotels().size());
    }

    @Test
    @Order(6)
    public void searchHotelByRatingTest(){
        HotelRq request = HotelRq.builder()
                .hotelSearch(HotelRq.HotelSearchRequest.builder()
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
        HotelRs response = hotelService.getAllHotelsByParams(request);
        Assertions.assertNotNull(response);
        Assertions.assertEquals("Hotel Тула", response.getHotels().getFirst().getName());
    }

    @Test
    @Order(7)
    public void searchHotelByRatingNoHotelTest(){
        HotelRq request = HotelRq.builder()
                .hotelSearch(HotelRq.HotelSearchRequest.builder()
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
        HotelRs response = hotelService.getAllHotelsByParams(request);
        Assertions.assertEquals(0, response.getHotels().size());
    }

    @Test
    @Order(8)
    public void searchHotelByDistanceTest(){
        HotelRq request = HotelRq.builder()
                .hotelSearch(HotelRq.HotelSearchRequest.builder()
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
        HotelRs response = hotelService.getAllHotelsByParams(request);
        Assertions.assertNotNull(response);
        Assertions.assertEquals("Hotel Тула", response.getHotels().getFirst().getName());
    }

    @Test
    @Order(9)
    public void searchHotelByDistanceNoHotelTest(){
        HotelRq request = HotelRq.builder()
                .hotelSearch(HotelRq.HotelSearchRequest.builder()
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
        HotelRs response = hotelService.getAllHotelsByParams(request);
        Assertions.assertEquals(0, response.getHotels().size());
    }

    @Test
    @Order(10)
    public void searchHotelByCoastTest(){
        HotelRq request = HotelRq.builder()
                .hotelSearch(HotelRq.HotelSearchRequest.builder()
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
        HotelRs response = hotelService.getAllHotelsByParams(request);
        Assertions.assertNotNull(response);
        Assertions.assertEquals("Hotel Тула", response.getHotels().getFirst().getName());
    }

    @Test
    @Order(11)
    public void searchHotelByCoastNoHotelTest(){
        HotelRq request = HotelRq.builder()
                .hotelSearch(HotelRq.HotelSearchRequest.builder()
                        .city("Тула")
                        .startReserved(new Date(1743465600000L))
                        .endReserved(new Date(1743811200000L))
                        .coastMin(200000L)
                        .coastMax(1000000L)
                        .build())
                .build();
        HotelRs response = hotelService.getAllHotelsByParams(request);
        Assertions.assertEquals(0, response.getHotels().size());
    }

    @Test
    @Order(12)
    public void searchHotelTest(){
        HotelRq request = HotelRq.builder()
                .hotelSearch(HotelRq.HotelSearchRequest.builder()
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
        HotelRs response = hotelService.getAllHotelsByParams(request);
        Assertions.assertNotNull(response);
        Assertions.assertEquals("Hotel Тула", response.getHotels().getFirst().getName());
    }

    @Test
    @Order(13)
    public void searchHotelNoHotelTest(){
        HotelRq request = HotelRq.builder()
                .hotelSearch(HotelRq.HotelSearchRequest.builder()
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
        HotelRs response = hotelService.getAllHotelsByParams(request);
        Assertions.assertEquals(0, response.getHotels().size());
    }

}
