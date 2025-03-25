package ru.project.reserved.system.db.app.service.dto.city;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CityResponse {

    private Long id;

    private String name;
}
