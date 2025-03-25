package ru.project.reserved.system.db.app.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.project.reserved.system.db.app.service.entity.Booking;

import java.util.UUID;

@Repository
public interface BookingRepository extends JpaRepository<Booking, UUID> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Booking b where b.id = :id")
    void deleteBookingById(UUID id);

    boolean existsBookingById(UUID id);
}
