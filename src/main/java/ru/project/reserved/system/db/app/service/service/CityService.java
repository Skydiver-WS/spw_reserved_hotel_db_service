package ru.project.reserved.system.db.app.service.service;

import ru.project.reserved.system.db.app.service.dto.city.CityRequest;
import ru.project.reserved.system.db.app.service.entity.City;

import java.util.Optional;

public interface CityService {

    Optional<City> findCity(String city);
    City saveCity(CityRequest cityRequest);
}
