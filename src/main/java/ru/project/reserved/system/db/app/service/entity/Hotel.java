package ru.project.reserved.system.db.app.service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name", "city", "address"})
})
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Version
    private Long version;

    private String userId;

    private String name;

    private String description;

    private String address;

    private Double distance;

    private Double rating;

    private String city;
    @Column(nullable = false, columnDefinition = "integer default 0")
    private Integer countApart = 0;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "hotel",
            fetch = FetchType.LAZY,
            orphanRemoval = true)
    private List<Room> roomList;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Photo> photos;

}
