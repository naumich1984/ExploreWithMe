package ru.practicum.ewm.controller._private;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.model.dto.ParticipationRequestDto;
import ru.practicum.ewm.service.RequestService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
public class RequestControllerPrivate {

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

    @PatchMapping("/users/{userId}/requests/{requestId}/cancel")
    public ResponseEntity<ParticipationRequestDto> cancelRequestPrivate(@PathVariable Long userId,
                                                                     @PathVariable Long requestId) {
        log.debug("PATCH /users/{userId}/requests/{requestId}/cancel");
        log.debug(" | userId: {}", userId);
        log.debug(" | requestId: {}", requestId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(requestService.cancelRequestPrivate(userId, requestId));
    }
}
