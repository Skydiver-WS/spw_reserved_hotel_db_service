package ru.project.reserved.system.db.app.service.dto.hotel;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.project.reserved.system.db.app.service.dto.city.CityRequest;
import ru.project.reserved.system.db.app.service.dto.type.SortType;
import ru.project.reserved.system.db.app.service.entity.Photo;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HotelRequest {

    private Long id;
    @NotNull
    private String userId;
    private String name;
    private String description;
    private String address;
    private Double distance;
    private Double rating;
    private List<Photo> photos;
    private CityRequest city;
    private HotelSearchRequest hotelSearch;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class HotelSearchRequest {
        private String city;
        private String hotelName;
        private Date startReserved;
        private Date endReserved;
        private SortType sortCoast;
        private Long coastMin;
        private Long coastMax;
        private SortType sortRating;
        private Double rating;
        private SortType sortDistance;
        private Double distance;
    }
}
