package ru.practicum.ewm.controller._private;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.model.dto.CommentDto;
import ru.practicum.ewm.model.dto.NewCommentDto;
import ru.practicum.ewm.service.CommentService;

import javax.validation.Valid;

@RestController
@Validated
@RequiredArgsConstructor
@Slf4j
public class CommentControllerPrivate {

    private final CommentService commentService;

    @PostMapping("/users/{userId}/events/{eventId}/comments")
    public ResponseEntity<CommentDto> addCommentPrivate(@RequestBody @Valid NewCommentDto newCommentDto,
                                                        @PathVariable Long userId,
                                                        @PathVariable Long eventId) {
        log.debug("POST /users/{userId}/events/{eventId}/comments");
        log.debug(" | userId: {}", userId);
        log.debug(" | eventId: {}", eventId);
        log.debug(" | text: {}", newCommentDto.getText());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentService.addCommentPrivate(newCommentDto, userId, eventId));
    }

    @DeleteMapping("/users/{userId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentPrivate(@PathVariable Long userId, @PathVariable Long commentId) {
        log.debug("DELETE /users/{userId}/comments/{commentId}");
        log.debug(" | userId: {}", userId);
        log.debug(" | commentId: {}", commentId);

        commentService.deleteCommentPrivate(userId, commentId);
    }

    @PatchMapping("/users/{userId}/comments/{commentId}")
    public ResponseEntity<CommentDto> updateCommentPrivate(@RequestBody @Valid NewCommentDto newCommentDto,
                                                        @PathVariable Long userId,
                                                        @PathVariable Long commentId) {
        log.debug("PATCH /users/{userId}/comments/{commentId}");
        log.debug(" | userId: {}", userId);
        log.debug(" | commentId: {}", commentId);
        log.debug(" | text: {}", newCommentDto.getText());

        return ResponseEntity.status(HttpStatus.OK)
                .body(commentService.updateCommentPrivate(newCommentDto, userId, commentId));
    }

    @GetMapping("/users/{userId}/comments/{commentId}")
    public ResponseEntity<CommentDto> getCommentPrivate(@PathVariable Long userId, @PathVariable Long commentId) {
        log.debug("GET /users/{userId}/comments/{commentId}");
        log.debug(" | userId: {}", userId);
        log.debug(" | commentId: {}", commentId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(commentService.getCommentPrivate(userId, commentId));
    }
}
