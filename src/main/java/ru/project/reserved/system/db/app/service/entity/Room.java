package ru.project.reserved.system.db.app.service.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.project.reserved.system.db.app.service.dto.ClassRoom;
import ru.project.reserved.system.db.app.service.dto.Status;

import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Room {

    @Id
    private Long id;

    private Long numberApart;

    private Double coast;

    private String description;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "room")
    private List<Photo> photoList;

    @Enumerated(EnumType.STRING)
    private Status status = Status.FREE;

    @Enumerated(EnumType.STRING)
    private ClassRoom classRoom;

    @ManyToOne
    private Hotel hotel;

}
