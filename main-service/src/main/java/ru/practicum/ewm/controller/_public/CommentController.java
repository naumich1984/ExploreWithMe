package ru.practicum.ewm.controller._public;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.model.dto.CommentDto;
import ru.practicum.ewm.service.CommentService;

import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@Validated
@RequiredArgsConstructor
@Slf4j
public class CommentController {

    private final CommentService commentService;

    @GetMapping("/events/{eventId}/comments")
    public ResponseEntity<List<CommentDto>> getCommentsPublic(@PathVariable Long eventId,
                                                              @RequestParam(defaultValue = "0", required = false) @Min(0) Integer from,
                                                              @RequestParam(defaultValue = "10", required = false) @Min(1) Integer size,
                                                              @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                                              @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                                              @RequestParam(required = false) String text) {
        log.debug("GET /events/{eventId}/comments");
        log.debug(" | eventId: {}", eventId);
        log.debug(" | from: {}", from);
        log.debug(" | size: {}", size);
        log.debug(" | rangeStart: {}", rangeStart);
        log.debug(" | rangeEnd: {}", rangeEnd);
        log.debug(" | text: {}", text);

        return ResponseEntity.status(HttpStatus.OK)
                .body(commentService.getCommentsPublic(eventId, from, size, rangeStart, rangeEnd, text));
    }
}
