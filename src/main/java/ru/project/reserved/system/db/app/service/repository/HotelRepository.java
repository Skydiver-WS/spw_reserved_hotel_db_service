package ru.project.reserved.system.db.app.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.project.reserved.system.db.app.service.entity.Hotel;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    List<Hotel> findHotelsByCity(String cityName);

    @Query("SELECT h.id, h.countApart, SIZE(h.roomList) FROM Hotel h")
    List<Long[]> findAllWithRoomCount();
    List<Hotel> findAllByIdIn(List<Long> ids);
    boolean existsByNameAndCity(String name, String cityName);

}
