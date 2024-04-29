package ru.practicum.ewm.controller._private;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.model.dto.EventFullDto;
import ru.practicum.ewm.model.dto.EventShortDto;
import ru.practicum.ewm.model.dto.NewEventDto;
import ru.practicum.ewm.model.dto.ParticipationRequestDto;
import ru.practicum.ewm.model.request.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.model.request.EventRequestStatusUpdateResult;
import ru.practicum.ewm.model.request.UpdateEventUserRequest;
import ru.practicum.ewm.service.EventService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
public class EventControllerPrivate {

    private final EventService eventService;

    @PostMapping("/users/{userId}/events")
    public ResponseEntity<EventFullDto> addEventPrivate(@RequestBody @Valid NewEventDto newEventDto, @PathVariable Long userId) {
        log.debug("POST /users/{userId}/events");
        log.debug(" | userId: {}", userId);
        log.debug(" | title: {}", newEventDto.getTitle());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(eventService.addEventPrivate(newEventDto, userId));
    }

    @GetMapping("/users/{userId}/events")
    public ResponseEntity<List<EventShortDto>> getEventsPrivate(@PathVariable Long userId,
                                                                @RequestParam(required = false, defaultValue = "0") Integer from,
                                                                @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.debug("GET /users/{userId}/events");
        log.debug(" | userId: {}", userId);
        log.debug(" | from: {}", from);
        log.debug(" | size: {}", size);

        return ResponseEntity.status(HttpStatus.OK)
                .body(eventService.getEventsPrivate(userId, from, size));
    }

    @GetMapping("/users/{userId}/events/{eventId}")
    public ResponseEntity<EventFullDto> getEventFullPrivate(@PathVariable Long userId, @PathVariable Long eventId) {
        log.debug("GET /users/{userId}/events/{eventId}");
        log.debug(" | userId: {}", userId);
        log.debug(" | eventId: {}", eventId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(eventService.getEventFullPrivate(userId, eventId));
    }

    @PatchMapping("/users/{userId}/events/{eventId}")
    public ResponseEntity<EventFullDto> updateEventPrivate(@RequestBody @Valid UpdateEventUserRequest updateEventUserRequest,
                                                           @PathVariable Long userId, @PathVariable Long eventId) {
        log.debug("PATCH /users/{userId}/events/{eventId}");
        log.debug(" | userId: {}", userId);
        log.debug(" | eventId: {}", eventId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(eventService.updateEvent(updateEventUserRequest, userId, eventId));
    }

    @GetMapping("/users/{userId}/events/{eventId}/requests")
    public ResponseEntity<List<ParticipationRequestDto>> getEventRequestsPrivate(@PathVariable Long userId, @PathVariable Long eventId) {
        log.debug("GET /users/{userId}/events/{eventId}/requests");
        log.debug(" | userId: {}", userId);
        log.debug(" | eventId: {}", eventId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(eventService.getEventRequestsPrivate(userId, eventId));
    }

    @PatchMapping("/users/{userId}/events/{eventId}/requests")
    public ResponseEntity<EventRequestStatusUpdateResult> updateEventRequestsPrivate(
            @RequestBody @Valid EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest,
            @PathVariable Long userId, @PathVariable Long eventId) {
        log.debug("PATCH /users/{userId}/events/{eventId}/requests");
        log.debug(" | userId: {}", userId);
        log.debug(" | eventId: {}", eventId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(eventService.updateEventRequestsPrivate(eventRequestStatusUpdateRequest, userId, eventId));
    }
}
