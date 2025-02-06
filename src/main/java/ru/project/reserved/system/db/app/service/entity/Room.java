package ru.project.reserved.system.db.app.service.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.project.reserved.system.db.app.service.dto.type.ClassRoomType;
import ru.project.reserved.system.db.app.service.dto.type.StatusType;

import java.util.Date;
import java.util.List;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long numberApart;

    private Double coast;

    private String description;

    private StatusType status = StatusType.FREE;

    private Date startReserved;

    private Date endReserved;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Photo> photoList;

    @Enumerated(EnumType.STRING)
    private StatusType statusType = StatusType.FREE;

    @Enumerated(EnumType.STRING)
    private ClassRoomType classRoomType;

    @ManyToOne
    private Hotel hotel;

}
