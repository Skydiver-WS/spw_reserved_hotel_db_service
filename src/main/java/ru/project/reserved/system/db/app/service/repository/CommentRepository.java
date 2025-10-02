package ru.project.reserved.system.db.app.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.project.reserved.system.db.app.service.entity.Comment;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {

    boolean existsCommentById(UUID id);

    @Query("SELECT c.hotel.id, c.hotel.rating, AVG(c.estimation) FROM Comment c GROUP BY c.hotel.id")
    Optional<List<Object[]>> getHotelAvgRatings();
}
