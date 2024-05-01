package ru.practicum.ewm.controller._admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.model.dto.CommentDto;
import ru.practicum.ewm.service.CommentService;

@RestController
@Validated
@RequiredArgsConstructor
@Slf4j
public class CommentControllerAdmin {

    private final CommentService commentService;

    @DeleteMapping("/admin/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentAdmin(@PathVariable Long commentId) {
        log.debug("DELETE /admin/comments/{commentId}");
        log.debug(" | commentId: {}", commentId);

        commentService.deleteCommentAdmin(commentId);
    }

    @GetMapping("/admin/comments/{commentId}")
    public ResponseEntity<CommentDto> getCommentAdmin(@PathVariable Long commentId) {
        log.debug("GET /admin/comments/{commentId}");
        log.debug(" | commentId: {}", commentId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(commentService.getCommentAdmin(commentId));
    }

}
