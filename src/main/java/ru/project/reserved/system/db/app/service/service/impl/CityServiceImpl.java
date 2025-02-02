package ru.project.reserved.system.db.app.service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.project.reserved.system.db.app.service.entity.City;
import ru.project.reserved.system.db.app.service.repository.CityRepository;
import ru.project.reserved.system.db.app.service.service.CityService;

@RequiredArgsConstructor
@Slf4j
@Service
public class CityServiceImpl implements CityService {
    private final CityRepository cityRepository;

    @Override
    public City findCity(String city) {
        return cityRepository.findCityByName(city).orElse(null);
    }
}
