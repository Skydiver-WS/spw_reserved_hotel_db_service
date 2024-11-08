package ru.project.reserved.system.db.app.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.project.reserved.system.db.app.service.entity.City;

import java.util.List;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HotelResponse {
    private String name;
    private String description;
    private String address;
    private Double distance;
    private String rating;
    private Integer freeApart;
    private Integer countApart;
    private String errorMessage;
    private List<City> cityList;
}
