package ru.project.reserved.system.db.app.service.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.project.reserved.system.db.app.service.dto.type.ClassRoomType;

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

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Photo> photoList;

    @Enumerated(EnumType.STRING)
    private ClassRoomType classRoomType;

    @ManyToOne(fetch = FetchType.EAGER)
    private Hotel hotel;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "room")
    private List<Booking> bookings;



}
