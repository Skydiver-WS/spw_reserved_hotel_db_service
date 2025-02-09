package ru.project.reserved.system.db.app.service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.project.reserved.system.db.app.service.dto.city.CityRequest;
import ru.project.reserved.system.db.app.service.entity.Address;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    @Mapping(target = "address", source = "address.address" )
    Address addressFromCityRequest(CityRequest cityRequest);
}
