package ru.project.reserved.system.db.app.service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String photo;

    @ManyToOne(fetch = FetchType.LAZY)
    private Room room;

}
