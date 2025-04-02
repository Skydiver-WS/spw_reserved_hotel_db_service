package ru.project.reserved.system.db.app.service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.project.reserved.system.db.app.service.entity.Room;
import ru.project.reserved.system.db.app.service.repository.RoomRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SaveAllService {

    private final RoomRepository roomRepository;

    @Transactional
    @Async
    public void saveAll(List<Room> rooms) {
        log.info("Saving rooms {}", rooms.size());
        try {
            roomRepository.saveAll(rooms);
        } catch (Exception e) {
            e.getMessage();
            try {
                Thread.sleep(60000);
            roomRepository.saveAll(rooms);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
    }


}
