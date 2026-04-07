package ru.project.reserved.system.db.app.service.specification;

import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import ru.project.reserved.system.db.app.service.dto.type.ClassRoomType;
import ru.project.reserved.system.db.app.service.entity.Booking;
import ru.project.reserved.system.db.app.service.entity.Hotel;
import ru.project.reserved.system.db.app.service.entity.Room;

import java.util.Date;
import java.util.Objects;

@Component
public class RoomSearchSpecification {

    public static Specification<Room> searchRoomById(Long id) {
        return (root, query, criteriaBuilder) -> {
            if (id == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("id"), id);
        };
    }

    public static Specification<Room> searchRoomByCoast(Double coast) {
        return (root, query, criteriaBuilder) -> {
            if (Double.isNaN(coast)) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("coast"), coast);
        };
    }

    public static Specification<Room> searchRoomByClassRoom(ClassRoomType type) {
        return ((root, query, criteriaBuilder) -> {
            if (Objects.isNull(type)) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("classRoom"), type);
        });
    }

    public static Specification<Room> searchRoomByHotelId(Long hotelId) {
        return (root, query, criteriaBuilder) -> {
            if (hotelId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.join("hotel").get("id"), hotelId);
        };
    }

    public static Specification<Room> searchRoomByDate(Date startReserved, Date endReserved) {
        return (root, query, criteriaBuilder) -> {
            if (Objects.isNull(startReserved) || Objects.isNull(endReserved)) {
                return criteriaBuilder.conjunction();
            }
            assert query != null;
            query.distinct(true);
            Join<Room, Booking> bookings = root.join("bookings");
            return criteriaBuilder.and(
                    criteriaBuilder.lessThanOrEqualTo(bookings.get("startDate"), startReserved),
                    criteriaBuilder.greaterThanOrEqualTo(bookings.get("endDate"), endReserved)
            );
        };
    }


}
