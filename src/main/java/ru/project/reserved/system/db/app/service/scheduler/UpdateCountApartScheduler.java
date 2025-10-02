package ru.project.reserved.system.db.app.service.scheduler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.project.reserved.system.db.app.service.repository.HotelRepository;
import ru.project.reserved.system.db.app.service.repository.RoomRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Component
public class UpdateCountApartScheduler {

    private final HotelRepository hotelRepository;
    private final RoomRepository roomRepository;

    @Scheduled(cron = "${scheduler.count-apart}")
    @Async
    @Transactional
    public void scheduleTaskUpdateCountRoom() {
        log.info("Starting task update count apart");
        List<Long[]> hotels = hotelRepository.findAllWithRoomCount();
        hotels.removeIf(l -> l[1].equals(l[2]));
        if (!hotels.isEmpty()) {
            log.info("Updating count apart");
            Map<Long, Long[]> hotelIds = hotels.stream()
                    .collect(Collectors.toMap(
                            l -> l[0],
                            l -> Arrays.copyOf(l, l.length, Long[].class)
                    ));
            hotelRepository.findAllByIdIn(hotelIds.keySet().stream().toList())
                    .stream()
                    .peek(h -> {
                        Long[] data = hotelIds.get(h.getId());
                        h.setCountApart(Math.toIntExact(data[2]));
                    }).forEach(h -> {
                        log.debug("Updating count apart hotel {}", h.getId());
                        hotelRepository.save(h);
                        log.debug("Successful update count apart hotel {}", h.getId());
                    });
            log.info("Task update count apart is complete");
            return;
        }
        log.info("Data is count apart is actual");
    }
}
