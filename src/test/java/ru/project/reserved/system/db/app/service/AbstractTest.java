package ru.project.reserved.system.db.app.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;
import ru.project.reserved.system.db.app.service.dto.hotel.HotelRequest;
import ru.project.reserved.system.db.app.service.dto.room.RoomRequest;
import ru.project.reserved.system.db.app.service.dto.type.ClassRoomType;
import ru.project.reserved.system.db.app.service.entity.*;
import ru.project.reserved.system.db.app.service.listener.Listener;
import ru.project.reserved.system.db.app.service.mapper.HotelMapper;
import ru.project.reserved.system.db.app.service.mapper.RoomMapper;
import ru.project.reserved.system.db.app.service.repository.BookingRepository;
import ru.project.reserved.system.db.app.service.repository.CityRepository;
import ru.project.reserved.system.db.app.service.repository.HotelRepository;
import ru.project.reserved.system.db.app.service.repository.RoomRepository;
import ru.project.reserved.system.db.app.service.service.HotelService;
import ru.project.reserved.system.db.app.service.service.RoomService;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @MockitoBean
    protected Listener listener;

    @MockitoBean
    private KafkaTemplate<String, Object> kafkaTemplate;

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

    @BeforeEach
    protected void setUp() {
        createCities();
        List<City> cities = cityRepository.findAll();
        hotelRepository.saveAllAndFlush(new ArrayList<>(List.of(createHotel1(cities),
                createHotel2(cities),
                createHotel3(cities))));

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

        List<Room> rooms = new ArrayList<>(List.of(
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
        ));

        hotel.setRoomList(rooms);
        return hotel;
    }

    private Hotel createHotel2(List<City> cities) {
        Hotel hotel = new Hotel();
        hotel.setName("Hotel Тула");
        hotel.setPhotos(photos());
        hotel.setCity(cities.stream().filter(c -> c.getName().equals("Тула")).findFirst().orElseThrow());

        List<Room> rooms = new ArrayList<>(List.of(
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
        ));

        hotel.setRoomList(rooms);
        return hotel;
    }

    private Hotel createHotel3(List<City> cities) {
        Hotel hotel = new Hotel();
        hotel.setName("Hotel Пермь");
        hotel.setPhotos(photos());
        hotel.setCity(cities.stream().filter(c -> c.getName().equals("Пермь")).findFirst().orElseThrow());

        List<Room> rooms = new ArrayList<>(List.of(
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
        ));

        hotel.setRoomList(rooms);
        return hotel;
    }

    private List<Photo> photos() {
        return new ArrayList<>(List.of(Photo.builder()
                        .photo("TEST_PHOTO")
                        .build(),
                Photo.builder()
                        .photo("TEST_PHOTO")
                        .build()
                ,
                Photo.builder()
                        .photo("TEST_PHOTO")
                        .build()));
    }
}
