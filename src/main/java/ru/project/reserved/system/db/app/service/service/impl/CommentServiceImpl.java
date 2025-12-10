package ru.project.reserved.system.db.app.service.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.project.reserved.system.db.app.service.dto.comment.CommentRq;
import ru.project.reserved.system.db.app.service.dto.comment.CommentRs;
import ru.project.reserved.system.db.app.service.entity.Comment;
import ru.project.reserved.system.db.app.service.entity.Hotel;
import ru.project.reserved.system.db.app.service.exception.CommentException;
import ru.project.reserved.system.db.app.service.exception.HotelException;
import ru.project.reserved.system.db.app.service.mapper.CommentMapper;
import ru.project.reserved.system.db.app.service.repository.CommentRepository;
import ru.project.reserved.system.db.app.service.repository.HotelRepository;
import ru.project.reserved.system.db.app.service.service.CommentService;

import java.util.Objects;
import java.util.UUID;


@Slf4j
@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final HotelRepository hotelRepository;
    private final CommentMapper commentMapper;

    @Override
    public CommentRs createComment(CommentRq commentRq) {
        log.info("Create comment");
        Hotel hotel = hotelRepository.findById(commentRq.getHotelId())
                .orElseThrow(() -> new HotelException("Hotel by id " + commentRq.getHotelId() + " not found"));
        Comment comment = commentMapper.commentFromCommentRqAndHotel(commentRq, hotel);
        if (Objects.nonNull(commentRq.getPhotos())) {
            Comment finalComment = comment;
            commentRq.getPhotos().forEach(p -> p.setComment(finalComment));
            comment.setPhotos(commentRq.getPhotos());
        }
        try {
            comment = commentRepository.save(comment);
            log.info("Comment create success");
            return CommentRs.builder()
                    .commentId(comment.getId().toString()).build();
        } catch (Exception ex) {
            log.error("Comment not create");
            throw new CommentException("Comment not created. Message: " + ex.getMessage());
        }

    }

    @Override
    public CommentRs updateComment(CommentRq commentRq) {
        log.info("Update comment with id {}", commentRq.getCommentId());
        Comment comment = commentRepository.findById(UUID.fromString(commentRq.getCommentId()))
                .orElseThrow(() -> new CommentException("Comment by id " + commentRq.getCommentId() + "not found"));
        commentMapper.updateComment(comment, commentRq);
        if (Objects.nonNull(commentRq.getPhotos())) {
            commentRq.getPhotos().forEach(c -> c.setComment(comment));
            comment.getPhotos().addAll(commentRq.getPhotos());
        }
        try {
            commentRepository.save(comment);
            log.info("Comment update success");
            return CommentRs.builder()
                    .commentId(comment.getId().toString())
                    .message("Comment update success")
                    .build();
        } catch (Exception ex) {
            log.error("Comment by id {} not update", comment.getId());
            throw new CommentException("Comment by id " + commentRq.getCommentId() + " not update. Message: "
                    + ex.getMessage());
        }
    }

    @Override
    @Transactional
    public CommentRs deleteComment(CommentRq commentRq) {
        log.info("Delete comment with id {}", commentRq.getCommentId());
        Comment comment = commentRepository.findById(UUID.fromString(commentRq.getCommentId()))
                .orElseThrow(() -> new CommentException("Comment by id " + commentRq.getCommentId() + "not found"));
        commentRepository.deleteById(comment.getId());
        commentRepository.flush();
        log.info("Comment with id {} delete success", comment.getId());
        return CommentRs.builder()
                .message("Comment with id " + comment.getId() + " delete success")
                .build();
    }
}
