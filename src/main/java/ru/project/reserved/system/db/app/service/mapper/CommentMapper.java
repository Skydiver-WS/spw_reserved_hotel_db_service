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
    @Mapping(target = "photos", ignore = true)
    @Mapping(target = "hotel.photos", ignore = true)
    @Mapping(target = "comment", source = "rq.message")
    @Mapping(target = "created", expression = "java(new java.util.Date())")
    Comment commentFromCommentRqAndHotel(CommentRq rq, Hotel hotel);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "comment", source = "rq.message")
    @Mapping(target = "created", expression = "java(new java.util.Date())")
    void updateComment(@MappingTarget Comment comment, CommentRq rq);

    @Mapping(target = "message", source = "comment")
    CommentRs commentRsFromComment(Comment comment);
}
