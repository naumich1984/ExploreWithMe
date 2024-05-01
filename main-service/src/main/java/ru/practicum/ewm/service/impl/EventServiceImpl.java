package ru.practicum.ewm.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.ewm.dto.stats.StatsDtoIn;
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
import ru.practicum.ewm.model.mapper.EventMapper;
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
import ru.practicum.ewm.utility.StatsInfo;

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
    private final StatsInfo statsInfo;

    @Override
    @Transactional
    public EventFullDto addEventPrivate(NewEventDto newEventDto, Long userId) {
        log.debug("RUN addEvent");
        Event eventNew = EventMapper.toEvent(newEventDto, userId, EventState.PENDING);
        eventNew.getCategory().setName(categoryRepository.findById(newEventDto.getCategory())
                .orElseThrow(() -> new NotFoundException("Category with id=" + newEventDto.getCategory() + " was not found"))
                .getName());
        eventNew.getInitiator().setName(userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " was not found"))
                .getName());

        return EventMapper.toEventFullDto(eventRepository.save(eventNew), 0L, 0L);
    }

    @Override
    public List<EventShortDto> getEventsPrivate(Long userId, Integer from, Integer size) {
        log.debug("RUN getEventsPrivate");
        CommonPageRequest pageable = new CommonPageRequest(from, size);
        List<EventShortFlatDto> eventsPage = eventRepository.findAllEventsWithRequestsCountByInitiatorId(userId, pageable);

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

        Long views = statsInfo.getStats(eventFullFlatDto.getCreatedOn(), LocalDateTime.now(), List.of("/events/" + eventId), true);

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
        Long views = statsInfo.getStats(event.getCreatedOn(), LocalDateTime.now(), List.of("/events/" + eventId), true);

        return EventMapper.toEventFullDto(result, 0L, views);
    }

    @Override
    public List<ParticipationRequestDto> getEventRequestsPrivate(Long userId, Long eventId) {
        log.debug("RUN getEventRequestsPrivate");

        return requestRepository.findEventRequestsByUserIdAndEventId(userId, eventId).orElse(List.of());
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult updateEventRequestsPrivate(EventRequestStatusUpdateRequest eventRequests,
                                                                    Long userId, Long eventId) {
        log.debug("RUN updateEventRequestsPrivate");
        CommonPageRequest oneRow = new CommonPageRequest(0, 1);
        List<Long> wrongStateFlagO = requestRepository.findCountRequestsWithWrongState(eventRequests.getRequestIds(), oneRow);
        if (!wrongStateFlagO.isEmpty()) {
            throw new ValidationException("The request must have the status PENDING");
        }

        Optional<List<EventRequestsStatDto>> eventStatO = requestRepository.findEventRequestsInfo(eventRequests.getRequestIds(), userId, eventId);
        long countConfirmed = 0L;
        long participantLimit = 0L;
        boolean requestModeration = false;
        long countToConfirm;
        if (eventStatO.isPresent()) {
            countConfirmed = eventStatO.get().stream()
                    .filter(f -> f.getRequestStatus().equals(RequestStatus.CONFIRMED))
                    .map(EventRequestsStatDto::getCountRequests)
                    .findFirst()
                    .orElse(0L);
            participantLimit = eventStatO.get().get(0).getParticipantLimit();
            requestModeration = eventStatO.get().get(0).getRequestModeration();
        } else {
            throw new NotFoundException("Event with id=" + eventId + " was not found");
        }

        if (participantLimit == 0L || !requestModeration) {
            throw new ValidationException("The request does not require confirmation");
        }

        countToConfirm = participantLimit - countConfirmed - 1;
        if (countToConfirm == 0L) {
            throw new ValidationException("The request does not require confirmation");
        }

        List<Long> requestIdsToConfirm;
        List<Long> requestIdsToCancel;
        if (eventRequests.getStatus().equals(RequestStatus.CONFIRMED)) {
            requestIdsToConfirm = eventRequests.getRequestIds().stream()
                    .limit(participantLimit - countConfirmed)
                    .collect(Collectors.toList());

            requestIdsToCancel = eventRequests.getRequestIds().stream()
                    .filter(id -> !requestIdsToConfirm.contains(id))
                    .collect(Collectors.toList());
        } else {
            requestIdsToConfirm = List.of();
            requestIdsToCancel = eventRequests.getRequestIds();
        }

        List<ParticipationRequestDto> eventConfirmedRequests = List.of();
        List<ParticipationRequestDto> eventRejectedRequests = List.of();
        if (!requestIdsToConfirm.isEmpty()) {
            requestRepository.updateRequestStatusByIds(requestIdsToConfirm, RequestStatus.CONFIRMED);
            eventConfirmedRequests = requestRepository.findAllRequestByIds(requestIdsToConfirm);
        }
        if (!requestIdsToCancel.isEmpty()) {
            requestRepository.updateRequestStatusByIds(requestIdsToCancel, RequestStatus.REJECTED);
            eventRejectedRequests = requestRepository.findAllRequestByIds(requestIdsToCancel);
        }

        return new EventRequestStatusUpdateResult(eventConfirmedRequests, eventRejectedRequests);
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

        return eventFullDtos.stream().map(event -> EventMapper.toEventFullDtoFromFlat(event,
                        statsInfo.getStats(event.getCreatedOn(), LocalDateTime.now(), List.of("/events/" + event.getId()), true)))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EventFullDto updateEventAdmin(UpdateEventAdminRequest updateEventAdminRequest, Long eventId) {
        log.debug("RUN updateEventAdminPrivate");
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));

        boolean changeStatus = Optional.ofNullable(updateEventAdminRequest.getStateAction()).isPresent();
        if (changeStatus) {
            // событие можно отклонить, только если оно еще не опубликовано (Ожидается код ошибки 409)
            if (updateEventAdminRequest.getStateAction().equals(EventUpdateState.REJECT_EVENT)
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
        Long views = statsInfo.getStats(event.getCreatedOn(), LocalDateTime.now(), List.of("/events/" + eventId), true);

        return EventMapper.toEventFullDto(result, 0L, views);
    }

    @Override
    public List<EventShortDto> getEventsByFilterPublic(String text, List<Long> categories, Boolean paid,
                                                       LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                       Boolean onlyAvailable, SortEnum sort,
                                                       Integer from, Integer size, StatsDtoIn statsDtoIn) {
        log.debug("RUN getEventsByFilterPublic");
        if (rangeStart != null && rangeEnd != null) {
            if (rangeEnd.isBefore(rangeStart)) {
                throw new BadRequestException("rangeEnd must be after rangeStart");
            }
        }

        CommonPageRequest pageable = new CommonPageRequest(from, size);
        List<EventShortFlatDto> events = eventRepository.findEventsByFilterPublic(text, categories, paid, rangeStart, rangeEnd, pageable);
        statsInfo.addHits(statsDtoIn);
        List<EventShortFlatDto> eventsFiltered;
        if (Optional.ofNullable(onlyAvailable).orElse(false)) {
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
        EventFullFlatDto eventFullFlatDto = eventRepository.findEventByIdWithRequestCount(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " was not found"));
        statsInfo.addHits(statsDtoIn);

        return EventMapper.toEventFullDtoFromFlat(eventFullFlatDto,
                statsInfo.getStats(eventFullFlatDto.getCreatedOn(), LocalDateTime.now(), List.of(statsDtoIn.getUri()), true));
    }

}
