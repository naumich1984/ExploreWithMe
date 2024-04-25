package ru.practicum.ewm.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import ru.practicum.ewm.dto.stats.StatsDtoOut;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model._enum.EventState;
import ru.practicum.ewm.model.dto.*;
import ru.practicum.ewm.model.dto.mapper.EventMapper;
import ru.practicum.ewm.repository.CategoryRepository;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.RequestRepository;
import ru.practicum.ewm.repository.UserRepository;
import ru.practicum.ewm.service.EventService;
import ru.practicum.ewm.utility.CommonPageRequest;
import ru.practicum.ewm.client.stats.StatsClient;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final StatsClient statsClient;
    private final ObjectMapper objectMapper;

    @Override
    @Transactional
    public EventFullDto addEvent(NewEventDto newEventDto, Long userId) {
        log.debug("RUN addEvent");
        Event eventNew = EventMapper.toEvent(newEventDto, userId, EventState.PENDING);
        eventNew.getCategory().setName(categoryRepository.findById(newEventDto.getCategory())
                .orElseThrow(() -> new NotFoundException("Category with id=" + newEventDto.getCategory() + " was not found"))
                .getName());
        eventNew.getInitiator().setName(userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found"))
                .getName());


        if (!eventNew.getRequestModeration()) {
            LocalDateTime now = LocalDateTime.now();
            eventNew.setCreatedOn(now);
            eventNew.setPublishedOn(now);
            eventNew.setState(EventState.PUBLISHED);
        }

        return EventMapper.toEventFullDto(eventRepository.save(eventNew), 0L, 0L);
    }

    @Override
    public List<EventShortDto> getEventsPrivate(Long userId, Integer from, Integer size) {
        log.debug("RUN getEventsPrivate");
        CommonPageRequest pageable = new CommonPageRequest(from, size);
        List<EventShortFlatDto> eventsPage = eventRepository.findAllEventsWithRequestsCountByInitiatorId(userId, pageable);

        //List<String> uris = eventsPage.stream().map(event -> "/events/" + event.getId().toString()).collect(Collectors.toList());
        //ResponseEntity<Object> response = statsClient.getHits(LocalDateTime.now().minusDays(13), LocalDateTime.now(), uris, true);
        //List<StatsDtoOut> stats = objectMapper.convertValue(response.getBody(), new TypeReference<List<StatsDtoOut>>() {});

        return eventsPage.stream()
                .map(event -> EventMapper.toEventShortDtoFromFlat(event))
                .collect(Collectors.toList());
    }
}
