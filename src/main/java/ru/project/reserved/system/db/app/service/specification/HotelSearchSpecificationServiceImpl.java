package ru.project.reserved.system.db.app.service.specification;

import jakarta.persistence.criteria.Join;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.project.reserved.system.db.app.service.dto.type.ClassRoomType;
import ru.project.reserved.system.db.app.service.entity.Booking;
import ru.project.reserved.system.db.app.service.entity.Hotel;
import ru.project.reserved.system.db.app.service.entity.Room;

import java.util.Date;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class HotelSearchSpecificationServiceImpl {

    public static Specification<Hotel> hotelSearchById(Long id) {
        return (root, query, criteriaBuilder) -> {
            if (id == 0) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("id"), id);
        };
    }

    public static Specification<Hotel> hotelSearchByName(String name) {
        return (root, query, criteriaBuilder) -> {
            if (Strings.isBlank(name)) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(root.get("name"), name);
        };
    }

    public static Specification<Hotel> hotelSearchByDistance(Double distance) {
        return (root, query, criteriaBuilder) -> {
            if (distance.isNaN()) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("distance"), distance);
        };
    }

    public static Specification<Hotel> hotelSearchByRoomType(ClassRoomType classRoomType) {
        return (root, query, criteriaBuilder) -> {
            if (Objects.isNull(classRoomType)) {
                return criteriaBuilder.conjunction();
            }
            assert query != null;
            query.distinct(true);
            Join<Hotel, Room> rooms = root.join("roomList");
            return criteriaBuilder.equal(rooms.get("classRoomType"), classRoomType);
        };
    }

    public static Specification<Hotel> hotelSearchByRoomCoast(Double coastMin, Double coastMax) {
        return (root, query, criteriaBuilder) -> {
            if (coastMin.isNaN() && coastMax.isNaN()) {
                return criteriaBuilder.conjunction();
            }
            assert query != null;
            query.distinct(true);
            Join<Hotel, Room> rooms = root.join("coast");
            if (!Double.isNaN(coastMin) && !Double.isNaN(coastMax)) {
                // Между минимальной и максимальной ценой
                return criteriaBuilder.between(rooms.get("coast"), coastMin, coastMax);
            } else if (!Double.isNaN(coastMin)) {
                // Выше минимальной цены
                return criteriaBuilder.greaterThanOrEqualTo(rooms.get("coast"), coastMin);
            } else {
                // Ниже максимальной цены
                return criteriaBuilder.lessThanOrEqualTo(rooms.get("coast"), coastMax);
            }
        };
    }

    /**
     * Поиск отелей с бронированиями в указанные даты
     */
    public static Specification<Hotel> searchByHasBookingBetweenDates(Date startDate, Date endDate) {
        return (root, query, criteriaBuilder) -> {
            if (startDate == null && endDate == null) {
                return criteriaBuilder.conjunction();
            }

            assert query != null;
            query.distinct(true);
            Join<Hotel, Room> rooms = root.join("roomList");
            Join<Room, Booking> bookings = rooms.join("bookings");

            if (startDate != null && endDate != null) {
                // Бронирование пересекается с указанным периодом
                return criteriaBuilder.and(
                        criteriaBuilder.lessThanOrEqualTo(bookings.get("startDate"), endDate),
                        criteriaBuilder.greaterThanOrEqualTo(bookings.get("endDate"), startDate)
                );
            } else if (startDate != null) {
                // Бронирование начинается после указанной даты
                return criteriaBuilder.greaterThanOrEqualTo(bookings.get("startDate"), startDate);
            } else {
                // Бронирование заканчивается до указанной даты
                return criteriaBuilder.lessThanOrEqualTo(bookings.get("endDate"), endDate);
            }
        };
    }
}
