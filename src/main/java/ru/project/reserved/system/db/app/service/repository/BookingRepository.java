package ru.project.reserved.system.db.app.service.repository;

import jakarta.validation.constraints.Max;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.project.reserved.system.db.app.service.entity.Booking;
import ru.project.reserved.system.db.app.service.entity.Room;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {


    @Query("""
    SELECT r.id FROM Room r
    WHERE r.id IN :rooms AND NOT EXISTS (
        SELECT b.id FROM Booking b
        WHERE b.room.id = r.id
        AND b.startReserved < :endDate
        AND b.endReserved > :startDate
    )
""")
    List<Long> findRoomsIdsByRoomIdInAndDateNotOverlapping(
            @Param("rooms") List<Long> roomIds,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate
    );

    @Transactional
    @Modifying
    @Query("DELETE FROM Booking b where b.id = :id")
    void deleteBookingById(UUID id);

    boolean existsBookingById(UUID id);
}
