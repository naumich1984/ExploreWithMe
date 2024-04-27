package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.dto.stats.StatsDtoIn;
import ru.practicum.ewm.model._enum.SortEnum;
import ru.practicum.ewm.model.dto.EventFullDto;
import ru.practicum.ewm.model.dto.EventShortDto;
import ru.practicum.ewm.model.dto.NewEventDto;
import ru.practicum.ewm.model.dto.ParticipationRequestDto;
import ru.practicum.ewm.model.dto.mapper.EventMapper;
import ru.practicum.ewm.model.dto.mapper.UserMapper;
import ru.practicum.ewm.model.request.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.model.request.EventRequestStatusUpdateResult;
import ru.practicum.ewm.model.request.UpdateEventAdminRequest;
import ru.practicum.ewm.model.request.UpdateEventUserRequest;
import ru.practicum.ewm.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping("/users/{userId}/events")
    public ResponseEntity<EventFullDto> addEventPrivate(@RequestBody @Valid NewEventDto newEventDto, @PathVariable Long userId) {
        log.debug("POST /users/{userId}/events");
        log.debug(" | userId: {}", userId);
        log.debug(" | title: {}", newEventDto.getTitle());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(eventService.addEvent(newEventDto, userId));
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


    @GetMapping("/admin/events")
    public ResponseEntity<List<EventFullDto>> getEventsByFilterAdmin(@RequestParam(required = false) List<Long> users,
                                                                     @RequestParam(required = false) List<String> states,
                                                                     @RequestParam(required = false) List<Long> categories,
                                                                     @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                                                     @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                                                     @RequestParam(required = false, defaultValue = "0") Integer from,
                                                                     @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.debug("GET /admin/events");
        log.debug(" | from: {}", from);
        log.debug(" | size: {}", size);

        return ResponseEntity.status(HttpStatus.OK)
                .body(eventService.getEventsByFilterAdmin(users, states, categories, rangeStart, rangeEnd, from, size));
    }

    @PatchMapping("/admin/events/{eventId}")
    public ResponseEntity<EventFullDto> updateEventAdminPrivate(
            @RequestBody @Valid UpdateEventAdminRequest updateEventAdminRequest, @PathVariable Long eventId) {
        log.debug("PATCH /admin/events/{eventId}");
        log.debug(" | eventId: {}", eventId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(eventService.updateEventAdminPrivate(updateEventAdminRequest, eventId));
    }

    @GetMapping("/events")
    public ResponseEntity<List<EventShortDto>> getEventsByFilterPublic(@RequestParam(required = false) String text,
                                                                     @RequestParam(required = false) List<Long> categories,
                                                                     @RequestParam(required = false) Boolean paid,
                                                                     @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                                                     @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                                                     @RequestParam(required = false) Boolean onlyAvailable,
                                                                     @RequestParam(required = false) SortEnum sort,
                                                                     @RequestParam(required = false, defaultValue = "0") Integer from,
                                                                     @RequestParam(required = false, defaultValue = "10") Integer size,
                                                                     HttpServletRequest request) {
        log.debug("GET /events");
        log.debug(" | from: {}", from);
        log.debug(" | size: {}", size);
        log.debug(" | client ip: {}", request.getRemoteAddr());
        log.debug(" | endpoint path: {}", request.getRequestURI());
        StatsDtoIn statsDtoIn = new StatsDtoIn("ExploreWithMe", request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.OK)
                .body(eventService.getEventsByFilterPublic(text, categories, paid, rangeStart, rangeEnd, onlyAvailable,
                        sort, from, size, statsDtoIn));
    }

    @GetMapping("/events/{eventId}")
    public ResponseEntity<EventFullDto> getEventsByIdPublic(@PathVariable Long eventId, HttpServletRequest request) {
        log.debug("GET /events");
        log.debug(" | eventId: {}", eventId);
        log.debug(" | client ip: {}", request.getRemoteAddr());
        log.debug(" | endpoint path: {}", request.getRequestURI());
        StatsDtoIn statsDtoIn = new StatsDtoIn("ExploreWithMe", request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.OK)
                .body(eventService.getEventsByIdPublic(eventId, statsDtoIn));
    }

}
