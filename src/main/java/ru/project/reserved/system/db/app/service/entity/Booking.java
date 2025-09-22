package ru.project.reserved.system.db.app.service.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue
    private UUID id;
    private String userId;
    private Date startReserved;
    private Date endReserved;
    private Date created = new Date();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Version
    private Long version;
}
