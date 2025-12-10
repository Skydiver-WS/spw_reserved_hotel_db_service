package ru.project.reserved.system.db.app.service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Comment {

    @Id
    @GeneratedValue
    private UUID id;
    private String userId;
    @Column(columnDefinition = "TEXT")
    private String comment;
    private Double estimation;
    private Date created = new Date();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "comment")
    private List<Photo> photos;

    @ManyToOne(fetch = FetchType.LAZY)
    private Hotel hotel;
}
