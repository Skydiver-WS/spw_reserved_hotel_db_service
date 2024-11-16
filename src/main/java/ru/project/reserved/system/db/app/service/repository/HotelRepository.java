package ru.project.reserved.system.db.app.service.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.project.reserved.system.db.app.service.entity.City;
import ru.project.reserved.system.db.app.service.entity.Hotel;

import java.util.List;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    List<Hotel> findHotelsByName(String hotelName);
    List<Hotel> findHotelsByCityList(List<City> cityList);
    List<Hotel> findHotelsByRating(Double rating);
    List<Hotel> findHotelsByDistance(Double distance);
    Hotel findHotelByNameAndCityList(String hotelName, List<City> cityList);
}
