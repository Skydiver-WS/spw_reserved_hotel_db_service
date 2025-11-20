package ru.project.reserved.system.db.app.service.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Table(name = "users")
public class User {

    @Id
    @NotNull
    private UUID userId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Hotel hotel;
}
