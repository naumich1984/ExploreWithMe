package ru.practicum.ewm.service;

import ru.practicum.ewm.dto.stats.StatsDtoIn;
import ru.practicum.ewm.model._enum.SortEnum;
import ru.practicum.ewm.model.dto.EventFullDto;
import ru.practicum.ewm.model.dto.EventShortDto;
import ru.practicum.ewm.model.dto.NewEventDto;
import ru.practicum.ewm.model.dto.ParticipationRequestDto;
import ru.practicum.ewm.model.request.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.model.request.EventRequestStatusUpdateResult;
import ru.practicum.ewm.model.request.UpdateEventAdminRequest;
import ru.practicum.ewm.model.request.UpdateEventUserRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    EventFullDto addEventPrivate(NewEventDto newEventDto, Long userId);

    List<EventShortDto> getEventsPrivate(Long userId, Integer from, Integer size);

    EventFullDto getEventFullPrivate(Long userId, Long eventId);

    EventFullDto updateEvent(UpdateEventUserRequest updateEventUserRequest, Long userId, Long eventId);

    List<ParticipationRequestDto> getEventRequestsPrivate(Long userId, Long eventId);

    EventRequestStatusUpdateResult updateEventRequestsPrivate(EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest,
                                                              Long userId, Long eventId);

    List<EventFullDto> getEventsByFilterAdmin(List<Long> users, List<String> states, List<Long> categories,
                                              LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size);

    EventFullDto updateEventAdmin(UpdateEventAdminRequest updateEventAdminRequest, Long eventId);

    List<EventShortDto> getEventsByFilterPublic(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                                LocalDateTime rangeEnd, Boolean onlyAvailable, SortEnum sort,
                                                Integer from, Integer size, StatsDtoIn statsDtoIn);

    EventFullDto getEventsByIdPublic(Long eventId, StatsDtoIn statsDtoIn);
}
