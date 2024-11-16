package ru.project.reserved.system.db.app.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.project.reserved.system.db.app.service.dto.ClassRoomType;
import ru.project.reserved.system.db.app.service.dto.Status;
import ru.project.reserved.system.db.app.service.entity.Room;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {

    List<Room> findRoomsByClassRoomType(ClassRoomType classRoomType);;
    List<Room> findRoomsByStatus(Status status);

}
