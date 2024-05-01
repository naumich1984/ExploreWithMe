package ru.practicum.ewm.controller._admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewm.model.dto.EventFullDto;
import ru.practicum.ewm.model.request.UpdateEventAdminRequest;
import ru.practicum.ewm.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@Validated
public class EventControllerAdmin {

    private final EventService eventService;

    @GetMapping("/admin/events")
    public ResponseEntity<List<EventFullDto>> getEventsByFilterAdmin(@RequestParam(required = false) List<Long> users,
                                                                     @RequestParam(required = false) List<String> states,
                                                                     @RequestParam(required = false) List<Long> categories,
                                                                     @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                                                     @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                                                     @RequestParam(required = false, defaultValue = "0") @Min(0) Integer from,
                                                                     @RequestParam(required = false, defaultValue = "10") @Min(1) Integer size) {
        log.debug("GET /admin/events");
        log.debug(" | from: {}", from);
        log.debug(" | size: {}", size);

        return ResponseEntity.status(HttpStatus.OK)
                .body(eventService.getEventsByFilterAdmin(users, states, categories, rangeStart, rangeEnd, from, size));
    }

    @PatchMapping("/admin/events/{eventId}")
    public ResponseEntity<EventFullDto> updateEventAdmin(
            @RequestBody @Valid UpdateEventAdminRequest updateEventAdminRequest, @PathVariable Long eventId) {
        log.debug("PATCH /admin/events/{eventId}");
        log.debug(" | eventId: {}", eventId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(eventService.updateEventAdmin(updateEventAdminRequest, eventId));
    }
}
