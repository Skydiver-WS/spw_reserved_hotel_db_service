package ru.project.reserved.system.db.app.service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.project.reserved.system.db.app.service.dto.city.CityRequest;
import ru.project.reserved.system.db.app.service.entity.City;
import ru.project.reserved.system.db.app.service.mapper.CityMapper;
import ru.project.reserved.system.db.app.service.repository.CityRepository;
import ru.project.reserved.system.db.app.service.service.CityService;

import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@Service
public class CityServiceImpl implements CityService {
    private final CityRepository cityRepository;
    private final CityMapper cityMapper;

    @Override
    public Optional<City> findCity(String city) {
        return cityRepository.findCityByName(city);
    }

    @Override
    public City saveCity(CityRequest cityRequest) {
        City city = cityMapper.cityFromCityRequest(cityRequest);
        log.info("Saving city: {}", city.getName());
        return cityRepository.saveAndFlush(city);
    }
}
