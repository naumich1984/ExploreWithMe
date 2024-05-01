package ru.practicum.ewm.controller._public;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.ewm.dto.stats.StatsDtoIn;
import ru.practicum.ewm.model._enum.SortEnum;
import ru.practicum.ewm.model.dto.EventFullDto;
import ru.practicum.ewm.model.dto.EventShortDto;
import ru.practicum.ewm.service.EventService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
public class EventController {

    private final EventService eventService;

    @GetMapping("/events")
    public ResponseEntity<List<EventShortDto>> getEventsByFilterPublic(@RequestParam(required = false) String text,
                                                                     @RequestParam(required = false) List<Long> categories,
                                                                     @RequestParam(required = false) Boolean paid,
                                                                     @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                                                     @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                                                     @RequestParam(required = false, defaultValue = "false") Boolean onlyAvailable,
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
