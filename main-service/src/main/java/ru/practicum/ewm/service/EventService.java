package ru.practicum.ewm.service;

import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.dto.EventFullDto;
import ru.practicum.ewm.model.dto.EventShortDto;
import ru.practicum.ewm.model.dto.NewEventDto;

import java.util.List;

public interface EventService {

    EventFullDto addEvent(NewEventDto newEventDto, Long userId);

    List<EventShortDto> getEventsPrivate(Long userId, Integer from, Integer size);
}
