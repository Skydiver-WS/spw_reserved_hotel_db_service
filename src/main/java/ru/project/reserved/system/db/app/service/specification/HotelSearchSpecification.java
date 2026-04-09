package ru.project.reserved.system.db.app.service.specification;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import ru.project.reserved.system.db.app.service.dto.type.ClassRoomType;
import ru.project.reserved.system.db.app.service.entity.Booking;
import ru.project.reserved.system.db.app.service.entity.Hotel;
import ru.project.reserved.system.db.app.service.entity.Room;

import java.util.Date;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class HotelSearchSpecification {

    public static Specification<Hotel> hotelSearchById(Long id) {
        return (root, query, criteriaBuilder) -> {
            if (id == null || id == 0) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get(Hotel.Fields.id.name()), id);
        };
    }

    public static Specification<Hotel> hotelSearchByName(String name) {
        return (root, query, criteriaBuilder) -> {
            if (Strings.isBlank(name)) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.like(root.get(Hotel.Fields.name.name()), name);
        };
    }

    public static Specification<Hotel> hotelSearchByCity(String city) {
        return (root, query, criteriaBuilder) -> {
            if (Strings.isBlank(city)) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get(Hotel.Fields.city.name()), city);
        };
    }

    public static Specification<Hotel> hotelSearchByDistance(Double distance) {
        return (root, query, criteriaBuilder) -> {
            if (Objects.isNull(distance)) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.lessThanOrEqualTo(root.get(Hotel.Fields.distance.name()), distance);
        };
    }

    public static Specification<Hotel> hotelSearchByRating(Double rating) {
        return (root, query, criteriaBuilder) -> {
          if (Objects.isNull(rating)) {
              return criteriaBuilder.conjunction();
          }
          return criteriaBuilder.greaterThanOrEqualTo(root.get(Hotel.Fields.rating.name()), rating);
        };
    }

    public static Specification<Hotel> hotelSearchByRoomType(ClassRoomType classRoomType) {
        return (root, query, criteriaBuilder) -> {
            if (Objects.isNull(classRoomType)) {
                return criteriaBuilder.conjunction();
            }
            assert query != null;
            query.distinct(true);
            Join<Hotel, Room> rooms = root.join(Hotel.Fields.roomList.name());
            return criteriaBuilder.equal(rooms.get(Room.Fields.classRoomType.name()), classRoomType);
        };
    }

    public static Specification<Hotel> hotelSearchByRoomCoast(Long coastMin, Long coastMax) {
        return (root, query, criteriaBuilder) -> {
            if (coastMin == null && coastMax == null) {
                return criteriaBuilder.conjunction();
            }
            assert query != null;
            query.distinct(true);
            Join<Hotel, Room> rooms = root.join(Hotel.Fields.roomList.name());
            if (coastMin != null && coastMax != null) {
                // Между минимальной и максимальной ценой
                return criteriaBuilder.between(rooms.get(Room.Fields.coast.name()), coastMin, coastMax);
            } else if (coastMin != null) {
                // Выше минимальной цены
                return criteriaBuilder.greaterThanOrEqualTo(rooms.get(Room.Fields.coast.name()), coastMin);
            } else {
                // Ниже максимальной цены
                return criteriaBuilder.lessThanOrEqualTo(rooms.get(Room.Fields.coast.name()), coastMax);
            }
        };
    }

    /**
     * Поиск отелей с бронированиями в указанные даты
     */
    public static Specification<Hotel> searchByHasBookingBetweenDates(Date startDate, Date endDate) {
        return (root, query, cb) -> {
            if (startDate == null || endDate == null) {
                return cb.conjunction();
            }

            assert query != null;
            query.distinct(true);

            // подзапрос: ищем пересекающееся бронирование
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<Booking> booking = subquery.from(Booking.class);

            Join<Booking, Room> room = booking.join(Booking.Fields.room.name());
            Join<Room, Hotel> hotel = room.join(Room.Fields.hotel.name());

            subquery.select(cb.literal(1L))
                    .where(
                            cb.equal(hotel.get(Hotel.Fields.id.name()), root.get(Hotel.Fields.id.name())),
                            cb.lessThan(booking.get(Booking.Fields.startReserved.name()), endDate),
                            cb.greaterThan(booking.get(Booking.Fields.endReserved.name()), startDate)
                    );

            // ❗ НЕТ пересечений → отель подходит
            return cb.not(cb.exists(subquery));
        };
    }

}
