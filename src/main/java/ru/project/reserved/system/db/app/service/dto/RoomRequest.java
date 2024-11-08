package ru.project.reserved.system.db.app.service.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.project.reserved.system.db.app.service.entity.Photo;

import java.util.List;

@Builder
@Getter
@Setter
public class RoomRequest {

    private Integer id;

    private Long numberApart;

    private String description;

    private Status status;

    private ClassRoom classRoom;

    private List<Photo> photoList;
}
