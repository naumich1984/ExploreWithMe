package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.model.dto.ParticipationRequestDto;
import ru.practicum.ewm.service.RequestService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
public class RequestController {

    private final RequestService requestService;

    @GetMapping("/users/{userId}/requests")
    public ResponseEntity<List<ParticipationRequestDto>> getRequestsPrivate(@PathVariable Long userId) {
        log.debug("GET /users/{userId}/requests");
        log.debug(" | userId: {}", userId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(requestService.getRequestsPrivate(userId));
    }

    @PostMapping("/users/{userId}/requests")
    public ResponseEntity<ParticipationRequestDto> addRequestPrivate(@PathVariable Long userId,
                                                                     @RequestParam Long eventId) {
        log.debug("POST /users/{userId}/requests");
        log.debug(" | userId: {}", userId);
        log.debug(" | eventId: {}", eventId);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(requestService.addRequestPrivate(userId, eventId));
    }
}
