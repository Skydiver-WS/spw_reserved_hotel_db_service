package ru.project.reserved.system.db.app.service.service;

import ru.project.reserved.system.db.app.service.dto.comment.CommentRq;
import ru.project.reserved.system.db.app.service.dto.comment.CommentRs;

public interface CommentService {
    CommentRs createComment(CommentRq commentRq);
    CommentRs updateComment(CommentRq commentRq);
    CommentRs deleteComment(CommentRq commentRq);
}
