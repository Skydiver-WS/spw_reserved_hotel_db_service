package ru.project.reserved.system.db.app.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import ru.project.reserved.system.db.app.service.entity.*;
import ru.project.reserved.system.db.app.service.mapper.HotelMapper;
import ru.project.reserved.system.db.app.service.mapper.RoomMapper;
import ru.project.reserved.system.db.app.service.repository.BookingRepository;
import ru.project.reserved.system.db.app.service.repository.CityRepository;
import ru.project.reserved.system.db.app.service.repository.HotelRepository;
import ru.project.reserved.system.db.app.service.repository.RoomRepository;
import ru.project.reserved.system.db.app.service.service.HotelService;
import ru.project.reserved.system.db.app.service.service.RoomService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static ru.project.reserved.system.db.app.service.dto.type.ClassRoomType.*;

@Slf4j
@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = ReservedSystemDbAppServiceApplication.class
)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
@Transactional
public class AbstractTest {

    @Autowired
    protected HotelRepository hotelRepository;

    @Autowired
    protected RoomRepository roomRepository;

    @Autowired
    protected BookingRepository bookingRepository;

    @Autowired
    protected CityRepository cityRepository;

    @Autowired
    protected HotelService hotelService;

    @Autowired
    protected RoomService roomService;

    @Autowired
    protected RoomMapper roomMapper;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected HotelMapper hotelMapper;

    @PersistenceContext
    protected EntityManager entityManager;

    protected List<Booking> bookings = new ArrayList<>();

    @BeforeEach
    protected void setUp() {
        createCities();
        List<City> cities = cityRepository.findAll();
        List<Hotel> hotels = (new ArrayList<>(List.of(createHotel1(cities),
                createHotel2(cities),
                createHotel3(cities))));
        hotelRepository.saveAllAndFlush(hotels);
        createRooms();
        createBookings();
        bookings.addAll(bookingRepository.findAll());
    }

    private void createCities() {
        cityRepository.saveAllAndFlush(new ArrayList<>(List.of(City
                        .builder()
                        .name("Москва")
                        .build(),
                City
                        .builder()
                        .name("Тула")
                        .build(),
                City
                        .builder()
                        .name("Пермь")
                        .build())));
    }


    private Hotel createHotel1(List<City> cities) {
        Hotel hotel = new Hotel();
        hotel.setName("Hotel Москва");
        hotel.setPhotos(photos());
        hotel.setCity(cities.stream().filter(c -> c.getName().equals("Москва")).findFirst().orElseThrow());
        hotel.setRating(7.4);
        hotel.setDistance(4.8);
        return hotel;
    }

    private Hotel createHotel2(List<City> cities) {
        Hotel hotel = new Hotel();
        hotel.setName("Hotel Тула");
        hotel.setPhotos(photos());
        hotel.setCity(cities.stream().filter(c -> c.getName().equals("Тула")).findFirst().orElseThrow());
        hotel.setRating(2.0);
        hotel.setDistance(15.0);
        return hotel;
    }

    private Hotel createHotel3(List<City> cities) {
        Hotel hotel = new Hotel();
        hotel.setName("Hotel Пермь");
        hotel.setPhotos(photos());
        hotel.setCity(cities.stream().filter(c -> c.getName().equals("Пермь")).findFirst().orElseThrow());
        hotel.setRating(3.2);
        hotel.setDistance(4.5);
        return hotel;
    }

    private void createRooms() {
        hotelRepository.findAll().forEach(h -> {
            rooms().forEach(r -> {
                r.setHotel(h);
                roomRepository.saveAndFlush(r);
            });
        });

    }

    private List<Room> rooms() {
        return List.of(
                Room.builder()
                        .photoList(photos())
                        .coast(10000.0)
                        .classRoomType(BUSINESS)
                        .description("The best room")
                        .numberApart(1L)
                        .build(),
                Room.builder()
                        .photoList(photos())
                        .coast(100000.0)
                        .classRoomType(LUX)
                        .description("The best room")
                        .numberApart(2L)
                        .build(),
                Room.builder()
                        .photoList(photos())
                        .coast(8000.0)
                        .classRoomType(STANDARD)
                        .description("The best room")
                        .numberApart(3L)
                        .build(),
                Room.builder()
                        .photoList(photos())
                        .coast(1000.0)
                        .classRoomType(ECONOMY)
                        .description("The best room")
                        .numberApart(4L)
                        .build(),
                Room.builder()
                        .photoList(photos())
                        .coast(10000.0)
                        .classRoomType(BUSINESS)
                        .description("The best room")
                        .numberApart(1L)
                        .build(),
                Room.builder()
                        .photoList(photos())
                        .coast(100000.0)
                        .classRoomType(LUX)
                        .description("The best room")
                        .numberApart(2L)
                        .build(),
                Room.builder()
                        .photoList(photos())
                        .coast(8000.0)
                        .classRoomType(STANDARD)
                        .description("The best room")
                        .numberApart(3L)
                        .build(),
                Room.builder()
                        .photoList(photos())
                        .coast(1000.0)
                        .classRoomType(ECONOMY)
                        .description("The best room")
                        .numberApart(4L)
                        .build(),
                Room.builder()
                        .photoList(photos())
                        .coast(10000.0)
                        .classRoomType(BUSINESS)
                        .description("The best room")
                        .numberApart(1L)
                        .build(),
                Room.builder()
                        .photoList(photos())
                        .coast(100000.0)
                        .classRoomType(LUX)
                        .description("The best room")
                        .numberApart(2L)
                        .build(),
                Room.builder()
                        .photoList(photos())
                        .coast(8000.0)
                        .classRoomType(STANDARD)
                        .description("The best room")
                        .numberApart(3L)
                        .build(),
                Room.builder()
                        .photoList(photos())
                        .coast(1000.0)
                        .classRoomType(ECONOMY)
                        .description("The best room")
                        .numberApart(4L)
                        .build()
        );
    }

    private void createBookings() {
        Hotel hotel = hotelRepository.findAll().stream().filter(h -> h.getName().equals("Hotel Тула"))
                .findFirst().orElseThrow();
        List<Room> rooms = roomRepository.findRoomsByHotel(hotel);
        rooms.forEach(r -> {
            Booking booking = new Booking();
            booking.setRoom(r);
            booking.setStartReserved(new Date());
            booking.setEndReserved(new Date(LocalDateTime.now()
                    .plusDays(5)
                    .atZone(ZoneId.systemDefault()) // Учитываем часовой пояс
                    .toInstant()
                    .toEpochMilli()));
            bookingRepository.save(booking);
        });
    }

    private List<Photo> photos() {
        return new ArrayList<>(List.of(Photo.builder()
                        .photo("TEST_PHOTO")
                        .build(),
                Photo.builder()
                        .photo("TEST_PHOTO")
                        .build(),
                Photo.builder()
                        .photo("TEST_PHOTO")
                        .build()));
    }
}
