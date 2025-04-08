package ru.project.reserved.system.db.app.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.project.reserved.system.db.app.service.dto.type.ClassRoomType;
import ru.project.reserved.system.db.app.service.entity.Hotel;
import ru.project.reserved.system.db.app.service.entity.Room;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findRoomsByClassRoomType(ClassRoomType classRoomType);

    List<Room> findRoomsByHotel(Hotel hotel);

    boolean existsRoomByHotelIdAndId(Long hotelId, Long roomId);

    Integer countRoomByHotelId(Long hotelId);

    @Query("SELECT r.id, r.hotel.id FROM Room r WHERE r.hotel.id IN :hotelIds")
    List<Long[]> findRoomIdsByHotelIdIn(@Param("hotelIds") List<Long> hotelIds);

    @Query("SELECT r.id, r.hotel.id, r.coast FROM Room  r WHERE r.hotel.id IN :hotelIds")
    List<Object[]> findRoomsByCoast (@Param("hotelIds") List<Long> hotelIds);


    @Modifying
    @Transactional
    @Query("DELETE FROM Room r WHERE r.hotel.id = :hotelId AND r.id = :roomId")

    void deleteByHotelIdAndRoomId(@Param("hotelId") Long hotelId, @Param("roomId") Long roomId);

}
