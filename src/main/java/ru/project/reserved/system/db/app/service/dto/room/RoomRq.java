package ru.project.reserved.system.db.app.service.dto.room;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.project.reserved.system.db.app.service.dto.type.BookingOperationType;
import ru.project.reserved.system.db.app.service.dto.type.ClassRoomType;
import ru.project.reserved.system.db.app.service.dto.type.SortType;
import ru.project.reserved.system.db.app.service.dto.type.StatusType;
import ru.project.reserved.system.db.app.service.entity.Photo;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoomRq {

    private Long id;

    private Long hotelId;

    private Long numberApart;

    private String userId;

    private String description;

    private StatusType statusType;

    private ClassRoomType classRoomType;

    private Double coast;

    private List<Photo> photoList;

    private RoomSearchRequest roomSearch;

    private RoomBooking roomBooking;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RoomSearchRequest {
        @NotNull
        private Long hotelId;
        @NotNull
        private String hotelName;
        @NotNull
        private Date startReserved;
        @NotNull
        private Date endReserved;
        private SortType sortCoast;
        private Double coast;
        private ClassRoomType classRoomType;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RoomBooking {
        private Long hotelId;
        private Date startReserved;
        private Date endReserved;
        private ClassRoomType classRoomType;
        private UUID bookingId;
        @NotNull
        private BookingOperationType operationType;

    }
}
