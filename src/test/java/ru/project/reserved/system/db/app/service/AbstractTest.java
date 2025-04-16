package ru.project.reserved.system.db.app.service;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import ru.project.reserved.system.db.app.service.entity.Booking;
import ru.project.reserved.system.db.app.service.entity.Hotel;
import ru.project.reserved.system.db.app.service.entity.Room;
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

    @BeforeEach
    protected void setUp() {
       createHotel();
       createRoom();
       createBooking();
    }

    @SneakyThrows
    private void createHotel(){
        log.info("Creating Hotel");
        String jsonHotel = new String(Files.readAllBytes(new File("src/test/resources/hotel/hotel.json").toPath()));
        HotelRequest hotelRequest = objectMapper.readValue(jsonHotel, HotelRequest.class);
        Hotel hotel = hotelMapper.mappingHotelRequestToHotel(hotelRequest);
        cityRepository.save(hotel.getCity());
        hotelRepository.saveAndFlush(hotel);

        String jsonHotel2 = new String(Files.readAllBytes(new File("src/test/resources/hotel/hotel2.json").toPath()));
        HotelRequest hotelRequest2 = objectMapper.readValue(jsonHotel2, HotelRequest.class);
        Hotel hotel2 = hotelMapper.mappingHotelRequestToHotel(hotelRequest2);
        cityRepository.save(hotel2.getCity());
        hotelRepository.saveAndFlush(hotel2);
        log.info("Hotel created");
    }

    @SneakyThrows
    private void createRoom(){
        log.info("Creating Room");
        List<RoomRequest> roomRequest = objectMapper.readValue(new File("src/test/resources/room/room.json"),
                objectMapper.getTypeFactory().constructCollectionType(List.class, RoomRequest.class));
        List<Hotel> hotels = hotelRepository.findAll();
        List<Room> rooms = new ArrayList<>();
        int i = 0;
        while (i < hotels.size()) {
            Hotel hotel = hotels.get(i++);
            rooms = roomRequest.stream().map(r -> {
                Room room = roomMapper.roomResponseToRoom(r);
                room.setHotel(hotel);
                return room;
            }).toList();
        }
        roomRepository.saveAllAndFlush(rooms);
        log.info("Room created");
    }

    @SneakyThrows
    private void createBooking(){
        log.info("Creating Booking");
        List<RoomRequest> hotelRequestsBooking = objectMapper.readValue(new File("src/test/resources/booking/booking.json"),
                objectMapper.getTypeFactory().constructCollectionType(List.class, RoomRequest.class));
        List<Room> roomList = roomRepository.findAll();
        List<Booking> bookings = new ArrayList<>();
        int i = 0;
        while (i < roomList.size()) {
            Room room = roomList.get(i++);
            bookings = hotelRequestsBooking.stream().map(r -> Booking.builder()
                            .startReserved(r.getRoomBooking().getStartReserved())
                            .endReserved(r.getRoomBooking().getEndReserved())
                            .room(room)
                            .build())
                    .toList();
        }
        bookingRepository.saveAllAndFlush(bookings);
        log.info("Booking created");
    }
}
