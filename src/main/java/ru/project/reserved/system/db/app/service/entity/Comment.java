package ru.project.reserved.system.db.app.service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Comment {

    @Id
    @GeneratedValue
    private UUID id;
    private String userId;
    @Column(columnDefinition = "TEXT")
    private String comment;
    private Double estimation;
    private Date created;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Photo> photos;

    @ManyToOne
    private Hotel hotel;
}
