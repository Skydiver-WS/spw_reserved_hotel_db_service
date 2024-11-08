package ru.project.reserved.system.db.app.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.project.reserved.system.db.app.service.entity.City;

import java.util.List;

@Getter
@Setter
@Builder
public class HotelRequest {

    private Long id;
    private String name;
    private String description;
    private String address;
    private Double distance;
    private Double rating;
    private List<City> cityList;
}
