package ru.project.reserved.system.db.app.service.dto.hotel;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.project.reserved.system.db.app.service.dto.room.RoomResponse;
import ru.project.reserved.system.db.app.service.dto.city.CityResponse;
import ru.project.reserved.system.db.app.service.entity.Photo;

import java.util.List;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HotelResponse {
    private Long id;
    private String name;
    private String description;
    private String address;
    private Double distance;
    private Double minCoast;
    private Double rating;
    private Integer freeApart;
    private Integer countApart;
    private List<Photo> photos;
    private String errorMessage;
    private String message;
}