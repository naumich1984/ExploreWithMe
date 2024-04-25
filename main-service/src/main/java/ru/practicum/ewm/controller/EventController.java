package ru.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.model.dto.EventFullDto;
import ru.practicum.ewm.model.dto.EventShortDto;
import ru.practicum.ewm.model.dto.NewEventDto;
import ru.practicum.ewm.model.dto.mapper.EventMapper;
import ru.practicum.ewm.model.dto.mapper.UserMapper;
import ru.practicum.ewm.service.EventService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping("/users/{userId}/events")
    public ResponseEntity<EventFullDto> addEvent(@RequestBody @Valid NewEventDto newEventDto, @PathVariable Long userId) {
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
}
