package ru.project.reserved.system.db.app.service.dto.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.project.reserved.system.db.app.service.entity.Photo;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientRequest {

    private String firstName;
    private String lastName;
    private String middleName;
    private String email;
    private String phone;
    private List<Photo> documents;
}
