package ru.project.reserved.system.db.app.service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private String photo;

    private Double distance;

    private Double rating;

    @Column(nullable = false, columnDefinition = "integer default 0")
    private Integer freeApart;

    @Column(nullable = false, columnDefinition = "integer default 0")
    private Integer countApart;

    @OneToMany
    private List<City> cityList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "hotel")
    private List<Room> roomList;

}
