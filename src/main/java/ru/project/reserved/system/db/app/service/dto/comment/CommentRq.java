package ru.project.reserved.system.db.app.service.dto.comment;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
public class CommentRq {

    private String commentId;
    @NotNull
    private String userId;
    @NotNull
    private Long hotelId;
    @Min(1)
    @Max(10)
    private Double estimation;
    private String message;
    private List<Photo> photos;
}
