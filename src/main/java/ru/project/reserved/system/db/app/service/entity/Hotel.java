package ru.project.reserved.system.db.app.service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

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

    @Column(columnDefinition = "integer default 0")
    private Integer freeApart;

    @Column(columnDefinition = "integer default 0")
    private Integer countApart;

    @ManyToMany(fetch = FetchType.EAGER,
            cascade = {CascadeType.DETACH,
                    CascadeType.MERGE,
                    CascadeType.PERSIST,
                    CascadeType.REFRESH})
    @JoinTable(name = "hotel_city",
    joinColumns = @JoinColumn(name = "hotel_id"),
    inverseJoinColumns = @JoinColumn(name = "city_id"))
    private Set<City> cityList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "hotel",
            fetch = FetchType.EAGER,
            orphanRemoval = true)
    private List<Room> roomList;

}
