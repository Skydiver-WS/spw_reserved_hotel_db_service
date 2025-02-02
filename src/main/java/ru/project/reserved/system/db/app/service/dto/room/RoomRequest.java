package ru.project.reserved.system.db.app.service.dto.room;

import com.sun.jdi.ClassType;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import ru.project.reserved.system.db.app.service.dto.type.ClassRoomType;
import ru.project.reserved.system.db.app.service.dto.type.SortType;
import ru.project.reserved.system.db.app.service.dto.type.StatusType;
import ru.project.reserved.system.db.app.service.entity.Photo;
import ru.project.reserved.system.db.app.service.service.RoomSearchService;

import java.util.Date;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoomRequest {

    private Integer id;

    private Long numberApart;

    private String description;

    private StatusType statusType;

    private ClassRoomType classRoomType;

    private List<Photo> photoList;

    private RoomSearchRequest roomSearch;

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
        private ClassType classType;
    }
}
