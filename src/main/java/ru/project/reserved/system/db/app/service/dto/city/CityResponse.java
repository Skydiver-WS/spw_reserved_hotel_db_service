package ru.project.reserved.system.db.app.service.dto.city;

import lombok.*;
import ru.project.reserved.system.db.app.service.entity.Address;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CityResponse {

    private Long id;

    private String name;

    private List<Address> address;
}
