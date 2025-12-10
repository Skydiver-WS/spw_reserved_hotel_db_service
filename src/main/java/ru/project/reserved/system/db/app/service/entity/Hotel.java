package ru.project.reserved.system.db.app.service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
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

    private String name;

    private String description;

    private String address;

    private Double distance;

    private Double rating;

    private Long countComments;

    private String city;
    @Column(nullable = false, columnDefinition = "integer default 0")
    private Integer countApart = 0;


    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "hotel")
    private List<Room> roomList;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Photo> photos;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "hotel")
    private List<Comment> comments;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "hotel")
    @Column(nullable = false)
    private List<User> users;

}
