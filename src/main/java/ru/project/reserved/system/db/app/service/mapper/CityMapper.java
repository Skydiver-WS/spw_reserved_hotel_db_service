package ru.project.reserved.system.db.app.service.mapper;

import org.mapstruct.Mapper;
import ru.project.reserved.system.db.app.service.dto.city.CityRequest;
import ru.project.reserved.system.db.app.service.dto.city.CityResponse;
import ru.project.reserved.system.db.app.service.entity.City;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CityMapper {

    CityResponse cityToCityResponse(City city);
    List<CityResponse> cityListToCityResponseList(List<City> cityList);
    //List<City> listCityFromListCityRequest(List<CityRequest> cityRequestList);
    City cityToCity(City city);
    City cityFromCityRequest(CityRequest cityRequest);
}
