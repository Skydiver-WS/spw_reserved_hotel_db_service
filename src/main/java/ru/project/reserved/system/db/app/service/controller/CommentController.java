package ru.project.reserved.system.db.app.service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.project.reserved.system.db.app.service.dto.comment.CommentRq;
import ru.project.reserved.system.db.app.service.dto.comment.CommentRs;
import ru.project.reserved.system.db.app.service.service.CommentService;

@RestController
@RequestMapping("/api/v1/comment")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentRs> createComment(@RequestBody @Validated CommentRq commentRq){
        return ResponseEntity.ok(commentService.createComment(commentRq));
    }

    @PutMapping
    public ResponseEntity<CommentRs> updateComment(@RequestBody @Validated CommentRq commentRq){
        return ResponseEntity.ok(commentService.updateComment(commentRq));
    }

    @DeleteMapping
    public ResponseEntity<CommentRs> deleteComment(@RequestBody @Validated CommentRq commentRq){
        return ResponseEntity.ok(commentService.deleteComment(commentRq));
    }

}
