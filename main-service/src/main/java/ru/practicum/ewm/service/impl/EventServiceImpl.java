package ru.practicum.ewm.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.practicum.ewm.client.stats.StatsClient;
import ru.practicum.ewm.dto.EventRequestsStatDto;
import ru.practicum.ewm.dto.stats.StatsDtoIn;
import ru.practicum.ewm.dto.stats.StatsDtoOut;
import ru.practicum.ewm.exception.BadRequestException;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.exception.ValidationException;
import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.model._enum.EventState;
import ru.practicum.ewm.model._enum.EventUpdateState;
import ru.practicum.ewm.model._enum.RequestStatus;
import ru.practicum.ewm.model._enum.SortEnum;
import ru.practicum.ewm.model.dto.*;
import ru.practicum.ewm.model.dto.mapper.EventMapper;
import ru.practicum.ewm.model.request.EventRequestStatusUpdateRequest;
import ru.practicum.ewm.model.request.EventRequestStatusUpdateResult;
import ru.practicum.ewm.model.request.UpdateEventAdminRequest;
import ru.practicum.ewm.model.request.UpdateEventUserRequest;
import ru.practicum.ewm.repository.CategoryRepository;
import ru.practicum.ewm.repository.EventRepository;
import ru.practicum.ewm.repository.RequestRepository;
import ru.practicum.ewm.repository.UserRepository;
import ru.practicum.ewm.service.EventService;
import ru.practicum.ewm.utility.CommonPageRequest;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
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


        /*if (!eventNew.getRequestModeration()) {
            LocalDateTime now = LocalDateTime.now();
            eventNew.setCreatedOn(now);
            eventNew.setPublishedOn(now);
            eventNew.setState(EventState.PUBLISHED);
        }*/

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
                .map(EventMapper::toEventShortDtoFromFlat)
                .collect(Collectors.toList());
    }

    @Override
    public EventFullDto getEventFullPrivate(Long userId, Long eventId) {
        log.debug("RUN getEventFullPrivate");
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found"));

        EventFullFlatDto eventFullFlatDto = eventRepository.findEventByIdAndUserIdWithRequestCount(userId, eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));

        Long views = 0L;

        return EventMapper.toEventFullDtoFromFlat(eventFullFlatDto, views);
    }

    @Override
    @Transactional
    public EventFullDto updateEvent(UpdateEventUserRequest updateEventUserRequest, Long userId, Long eventId) {
        log.debug("RUN updateEvent");
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));
        User user = event.getInitiator();
        if (!user.getId().equals(userId)) {
            throw new NotFoundException("User with id=" + userId + " was not found");
        }
        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new ValidationException("Only pending or canceled events can be changed");
        }

        if (Optional.ofNullable(updateEventUserRequest.getStateAction()).isPresent() &&
                updateEventUserRequest.getStateAction().equals(EventUpdateState.CANCEL_REVIEW)) {
            event.setState(EventState.CANCELED);
        }

        if (Optional.ofNullable(updateEventUserRequest.getStateAction()).isPresent() &&
                updateEventUserRequest.getStateAction().equals(EventUpdateState.SEND_TO_REVIEW)) {
            event.setState(EventState.PENDING);
        }

        Long categoryId = event.getCategory().getId();
        String categoryName = event.getCategory().getName();
        Event result = eventRepository.save(EventMapper.toEventFromUpdateRequest(updateEventUserRequest, event));
        result.setCategory(Category.builder().id(categoryId).name(categoryName).build());

        return EventMapper.toEventFullDto(result, 0L, 0L);
    }

    @Override
    public List<ParticipationRequestDto> getEventRequestsPrivate(Long userId, Long eventId) {
        log.debug("RUN getEventRequestsPrivate");
        Optional<List<ParticipationRequestDto>> eventRequests = eventRepository.findAllEventRequestsByInitiatorIdAndEventId(userId, eventId);

        return eventRequests.orElse(List.of());
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult updateEventRequestsPrivate(EventRequestStatusUpdateRequest eventRequests,
                                                                    Long userId, Long eventId) {
        log.debug("RUN updateEventRequestsPrivate");
        Optional<EventRequestsStatDto> eventStatO = eventRepository.findEventRequestsInfo(eventRequests.getRequestIds(), userId, eventId);
        long countNotConfirmed = 0L;
        long participantLimit = 0L;
        boolean requestModeration = false;
        long countToConfirm;

        if (eventStatO.isPresent()) {
            EventRequestsStatDto eventStat = eventStatO.get();
            countNotConfirmed = (long) eventStat.getPendingRequests();
            participantLimit = eventStat.getParticipantLimit();
            requestModeration = eventStat.getRequestModeration();
        } else {
            throw new NotFoundException("Event with id=" + eventId + " was not found");
        }

        if (participantLimit == 0L || requestModeration) {

            return null;
        }

        countToConfirm = participantLimit - countNotConfirmed;
        if (countToConfirm == 0) {

            return null;
        }

        List<Long> requestIdsToConfirm = eventRequests.getRequestIds().stream()
                .limit(participantLimit - countNotConfirmed)
                .collect(Collectors.toList());

        List<Long> requestIdsToCancel = eventRequests.getRequestIds().stream()
                .filter(id -> !requestIdsToConfirm.contains(id))
                .collect(Collectors.toList());

        requestRepository.updateRequestStatusByIds(requestIdsToConfirm, RequestStatus.CONFIRMED);
        requestRepository.updateRequestStatusByIds(requestIdsToCancel, RequestStatus.REJECTED);

        List<ParticipationRequestDto> eventConfirmedRequests = requestRepository.findAllRequestByIds(requestIdsToConfirm);
        List<ParticipationRequestDto> eventRejectedRequests = requestRepository.findAllRequestByIds(requestIdsToConfirm);

        return new EventRequestStatusUpdateResult(eventConfirmedRequests, eventRejectedRequests);
        /*Optional<List<EventRequestsConfirmDto>> eventRequestsO =
                eventRepository.findEventRequestsInfoForConfirmation(eventRequestStatusUpdateRequest.getRequestIds(), userId, eventId);
        List<EventRequestsConfirmDto> eventRequests;
        Long countNotConfirmed;
        long countParticipantLimit;

        if (eventRequestsO.isPresent()) {
            eventRequests =  eventRequestsO.get();
            countNotConfirmed = (long) eventRequests.size();
            countParticipantLimit = (long) eventRequests.get(0).getParticipantLimit();
            if (countParticipantLimit == 0 || !eventRequests.get(0).getRequestModeration()) {

                //Подтверждение заявок не требуется
                return null;
            } else if (countNotConfirmed.equals(countParticipantLimit)) {

                //достигнут лимит 409
                return null;
            }

            long countAvailable = countParticipantLimit - countNotConfirmed;
            eventRequests
        }*/

    }

    @Override
    public List<EventFullDto> getEventsByFilterAdmin(List<Long> users, List<String> states, List<Long> categories,
                                                     LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size) {
        log.debug("RUN getEventsByFilterAdmin");
        CommonPageRequest pageable = new CommonPageRequest(from, size);
        List<EventState> eventStateList = null;
        if (states != null) {
            eventStateList = states.stream().map(EventState::valueOf).collect(Collectors.toList());
        }

        List<EventFullFlatDto> eventFullDtos = eventRepository.findEventsByFilter(users, eventStateList, categories, rangeStart, rangeEnd, pageable);

        return eventFullDtos.stream().map(event -> EventMapper.toEventFullDtoFromFlat(event, 0L)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventFullDto updateEventAdminPrivate(UpdateEventAdminRequest updateEventAdminRequest, Long eventId) {
        log.debug("RUN updateEventAdminPrivate");
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));

        boolean changeStatus = Optional.ofNullable(updateEventAdminRequest.getStateAction()).isPresent();
        if (changeStatus) {
            // событие можно отклонить, только если оно еще не опубликовано (Ожидается код ошибки 409)
            if ( updateEventAdminRequest.getStateAction().equals(EventUpdateState.REJECT_EVENT)
                    && event.getState().equals(EventState.PUBLISHED)) {
                throw new ValidationException("Conflict");
            }

            // событие можно публиковать, только если оно в состоянии ожидания публикации (Ожидается код ошибки 409)
            if (updateEventAdminRequest.getStateAction().equals(EventUpdateState.PUBLISH_EVENT)
                    && !event.getState().equals(EventState.PENDING)) {
                throw new ValidationException("Conflict");
            }

            if (updateEventAdminRequest.getStateAction().equals(EventUpdateState.PUBLISH_EVENT)) {
                event.setState(EventState.PUBLISHED);
                event.setPublishedOn(LocalDateTime.now());
            }

            if (updateEventAdminRequest.getStateAction().equals(EventUpdateState.REJECT_EVENT)) {
                event.setState(EventState.CANCELED);
            }
        }

        Long categoryId = event.getCategory().getId();
        String categoryName = event.getCategory().getName();
        Event result = eventRepository.save(EventMapper.toEventFromUpdateAdminRequest(updateEventAdminRequest, event));
        result.setCategory(Category.builder().id(categoryId).name(categoryName).build());

        return EventMapper.toEventFullDto(result, 0L, 0L);
    }

    @Override
    public List<EventShortDto> getEventsByFilterPublic(String text, List<Long> categories, Boolean paid,
                                                       LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                       Boolean onlyAvailable, SortEnum sort,
                                                       Integer from, Integer size, StatsDtoIn statsDtoIn) {
        log.debug("RUN getEventsByFilterPublic");
        statsClient.addHits(statsDtoIn);

        if (rangeStart != null && rangeEnd != null) {
            if (rangeEnd.isBefore(rangeStart)) {
                throw new BadRequestException("rangeEnd must be after rangeStart");
            }
        }

        CommonPageRequest pageable = new CommonPageRequest(from, size);
        List<EventShortFlatDto> events = eventRepository.findEventsByFilterPublic(text, categories, paid, rangeStart, rangeEnd, pageable);
        List<EventShortFlatDto> eventsFiltered;
        if(Optional.ofNullable(onlyAvailable).orElse(false)) {
            eventsFiltered = events.stream()
                    .filter(e -> e.getParticipantLimit() > e.getConfirmedRequests() || e.getParticipantLimit() == 0)
                    .collect(Collectors.toList());
        } else {
            eventsFiltered = events;
        }

        if (Optional.ofNullable(sort).orElse(SortEnum.EVENT_DATE).equals(SortEnum.EVENT_DATE)) {
            return eventsFiltered.stream().map(EventMapper::toEventShortDtoFromFlat)
                    .sorted(Comparator.comparing(EventShortDto::getEventDate))
                    .collect(Collectors.toList());
        } else {
            return eventsFiltered.stream().map(EventMapper::toEventShortDtoFromFlat)
                    .sorted(Comparator.comparing(EventShortDto::getViews))
                    .collect(Collectors.toList());
        }

    }

    @Override
    public EventFullDto getEventsByIdPublic(Long eventId, StatsDtoIn statsDtoIn) {
        log.debug("RUN updateEventAdminPrivate");
        Long views = 0L;

        EventFullFlatDto eventFullFlatDto = eventRepository.findEventByIdWithRequestCount(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));

        statsDtoIn.setTimestamp(LocalDateTime.now());
        statsClient.addHits(statsDtoIn);
        ResponseEntity<Object> response = statsClient.getHits(eventFullFlatDto.getCreatedOn(), LocalDateTime.now(),
                List.of(statsDtoIn.getUri()), true);
        List<StatsDtoOut> stats = objectMapper.convertValue(response.getBody(), new TypeReference<List<StatsDtoOut>>() {});
        if (!stats.isEmpty()) {
            views = stats.get(0).getHits();
        }

        return EventMapper.toEventFullDtoFromFlat(eventFullFlatDto, views);
    }
}
