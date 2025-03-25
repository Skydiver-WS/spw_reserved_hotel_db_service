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
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private String address;

    private Double distance;

    private Double rating;

    @Column(nullable = false, columnDefinition = "integer default 0")
    private Integer countApart = 0;

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

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Photo> photos;

}
