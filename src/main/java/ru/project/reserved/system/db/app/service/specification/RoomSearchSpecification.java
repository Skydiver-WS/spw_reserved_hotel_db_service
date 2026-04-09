package ru.project.reserved.system.db.app.service.specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import ru.project.reserved.system.db.app.service.dto.type.ClassRoomType;
import ru.project.reserved.system.db.app.service.entity.Booking;
import ru.project.reserved.system.db.app.service.entity.Hotel;
import ru.project.reserved.system.db.app.service.entity.Room;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Component
public class RoomSearchSpecification {

    public static Specification<Room> searchRoomById(Long id) {
        return (root, query, criteriaBuilder) -> {
            if (id == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get(Room.Fields.id.name()), id);
        };
    }

    public static Specification<Room> searchRoomByCoast(Double coastMin, Double coastMax) {
        return (root, query, criteriaBuilder) -> {
            if (coastMin == null && coastMax == null) {
                return criteriaBuilder.conjunction();
            }
            if (coastMin != null && coastMax != null) {
                // Между минимальной и максимальной ценой
                return criteriaBuilder.between(root.get(Room.Fields.coast.name()), coastMin, coastMax);
            } else if (coastMin != null) {
                // Выше минимальной цены
                return criteriaBuilder.greaterThanOrEqualTo(root.get(Room.Fields.coast.name()), coastMin);
            } else {
                // Ниже максимальной цены
                return criteriaBuilder.lessThanOrEqualTo(root.get(Room.Fields.coast.name()), coastMax);
            }
        };
    }

    public static Specification<Room> searchRoomByClassRoom(ClassRoomType type) {
        return ((root, query, criteriaBuilder) -> {
            if (Objects.isNull(type)) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get(Room.Fields.classRoomType.name()), type);
        });
    }

    public static Specification<Room> searchRoomByHotelId(Long hotelId) {
        return (root, query, criteriaBuilder) -> {
            if (hotelId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.join(Room.Fields.hotel.name()).get(Hotel.Fields.id.name()), hotelId);
        };
    }

    public static Specification<Room> searchRoomByHotelName(String hotelName) {
        return (root, query, criteriaBuilder) -> {
            if (Strings.isBlank(hotelName)) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.join(Room.Fields.hotel.name())
                    .get(Hotel.Fields.name.name()), hotelName);
        };
    }

    public static Specification<Room> searchRoomByBookingId(UUID bookingId) {
        return (root, query, criteriaBuilder) -> {
            if (Objects.isNull(bookingId)) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.join(Room.Fields.bookings.name())
                    .get(Booking.Fields.id.name()), bookingId);
        };
    }

    public static Specification<Room> searchRoomByDate(Date startReserved, Date endReserved) {
        return (root, query, cb) -> {
            if (startReserved == null || endReserved == null) {
                return cb.conjunction();
            }

            query.distinct(true);

            // подзапрос: ищем пересекающееся бронирование
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<Booking> booking = subquery.from(Booking.class);

            Join<Booking, Room> bookingRoom = booking.join(Booking.Fields.room.name());

            subquery.select(cb.literal(1L))
                    .where(
                            // ✅ правильная связь
                            cb.equal(bookingRoom, root),

                            // ✅ правильное пересечение дат
                            cb.lessThan(booking.get(Booking.Fields.startReserved.name()), endReserved),
                            cb.greaterThan(booking.get(Booking.Fields.endReserved.name()), startReserved)
                    );

            // ❗ НЕТ пересечений → комната свободна
            return cb.not(cb.exists(subquery));
        };
    }


}
