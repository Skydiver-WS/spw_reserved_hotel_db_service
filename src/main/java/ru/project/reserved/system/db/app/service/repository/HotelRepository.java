package ru.project.reserved.system.db.app.service.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.project.reserved.system.db.app.service.entity.City;
import ru.project.reserved.system.db.app.service.entity.Hotel;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    List<Hotel> findHotelsByCity_Name(String cityName);

    @Query("SELECT h.id, h.countApart, SIZE(h.roomList) FROM Hotel h")
    List<Long[]> findAllWithRoomCount();
    List<Hotel> findAllByIdIn(List<Long> ids);
    boolean existsByNameAndCity_Name(String name, String cityName);

}
