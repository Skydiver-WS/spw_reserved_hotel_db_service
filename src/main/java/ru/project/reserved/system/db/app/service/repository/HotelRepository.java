package ru.project.reserved.system.db.app.service.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.project.reserved.system.db.app.service.entity.City;
import ru.project.reserved.system.db.app.service.entity.Hotel;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    List<Hotel> findHotelsByName(String hotelName);
    Optional<Hotel> findHotelByName(String hotelName);
    List<Hotel> findHotelsByCityList(Set<City> cityList);
    List<Hotel> findHotelsByRatingGreaterThanEqual(Double rating);
    List<Hotel> findHotelsByRatingLessThanEqual(Double rating);
    List<Hotel> findHotelsByRatingBetween(Double minRating, Double maxRating);
    List<Hotel> findHotelsByRating(Double rating);
    List<Hotel> findHotelsByDistance(Double distance);

}
