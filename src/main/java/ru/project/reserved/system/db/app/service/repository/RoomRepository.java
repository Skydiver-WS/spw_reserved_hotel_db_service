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
import java.util.Optional;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findRoomsByHotel(Hotel hotel);

    boolean existsRoomByHotelIdAndId(Long hotelId, Long roomId);

    Optional<Room> findRoomByIdAndHotel_Id(Long roomId, Long hotelId);

    @Query("SELECT r.id, r.hotel.id FROM Room r WHERE r.hotel.id IN :hotelIds")
    List<Long[]> findRoomIdsByHotelIdIn(@Param("hotelIds") List<Long> hotelIds);

    @Query("SELECT r.id, r.hotel.id, r.coast FROM Room  r WHERE r.hotel.id IN :hotelIds")
    List<Object[]> findRoomsByCoast (@Param("hotelIds") List<Long> hotelIds);

    @Query("SELECT r.id FROM Room r WHERE  r.hotel IN :hotel")
    List<Long> findIdsRoomByHotel(@Param("hotel") Hotel hotel);

    @Query("SELECT r.id FROM Room r WHERE r.coast <= :coast")
    List<Long> findIdsRoomsByCoast(@Param("coast") Double coast);

    @Query("SELECT r.id FROM Room r WHERE r.classRoomType IN :class")
    List<Long> findIdsRoomsByClassRoom(@Param("class") ClassRoomType classRoomType);

    @Query("SELECT r FROM Room  r WHERE r.id IN :ids ORDER BY r.coast ASC")
    List<Room> findRoomsByIds(@Param("ids") List<Long> ids);


    @Modifying
    @Transactional
    @Query("DELETE FROM Room r WHERE r.hotel.id = :hotelId AND r.id = :roomId")
    void deleteByHotelIdAndRoomId(@Param("hotelId") Long hotelId, @Param("roomId") Long roomId);

}
