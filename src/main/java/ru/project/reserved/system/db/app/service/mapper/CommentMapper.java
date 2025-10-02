package ru.project.reserved.system.db.app.service.mapper;

import org.mapstruct.*;
import ru.project.reserved.system.db.app.service.dto.comment.CommentRq;
import ru.project.reserved.system.db.app.service.dto.comment.CommentRs;
import ru.project.reserved.system.db.app.service.entity.Comment;
import ru.project.reserved.system.db.app.service.entity.Hotel;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "hotel", source = "hotel")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", source = "rq.userId")
    @Mapping(target = "photos", source = "rq.photos")
    Comment commentFromCommentRqAndHotel(CommentRq rq, Hotel hotel);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateComment(@MappingTarget Comment comment, CommentRq rq);

    CommentRs commentRsFromComment(Comment comment);
}
