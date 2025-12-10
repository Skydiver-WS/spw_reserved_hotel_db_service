package ru.project.reserved.system.db.app.service.entity;

import jakarta.persistence.*;
import lombok.*;
import ru.project.reserved.system.db.app.service.dto.type.ClassRoomType;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"numberApart", "hotel_id"})
})
@Data
@Builder
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Version
    private Long version;

    private Long numberApart;

    private Double coast;

    private String description;

    @Enumerated(EnumType.STRING)
    private ClassRoomType classRoomType;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "room")
    private List<Photo> photoList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "room", orphanRemoval = true)
    private List<Booking> bookings;
}
