package ru.practicum.ewm.model.dto.mapper;

import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.model._enum.EventState;
import ru.practicum.ewm.model._enum.EventUpdateState;
import ru.practicum.ewm.model.dto.*;
import ru.practicum.ewm.model.request.UpdateEventAdminRequest;
import ru.practicum.ewm.model.request.UpdateEventUserRequest;

import java.time.LocalDateTime;
import java.util.Optional;

public class EventMapper {

    public static Event toEvent(NewEventDto newEventDto, Long userId, EventState eventState) {

        return Event.builder()
                .annotation(newEventDto.getAnnotation())
                .category(Category.builder().id(newEventDto.getCategory()).build())
                .description(newEventDto.getDescription())
                .eventDate(newEventDto.getEventDate())
                .lat(newEventDto.getLocation().getLat())
                .lon(newEventDto.getLocation().getLon())
                .paid(Optional.ofNullable(newEventDto.getPaid()).orElse(false))
                .participantLimit(Optional.ofNullable(newEventDto.getParticipantLimit()).orElse(0))
                .requestModeration(Optional.ofNullable(newEventDto.getRequestModeration()).orElse(true))
                .title(newEventDto.getTitle())
                .initiator(User.builder().id(userId).build())
                .createdOn(null)
                .state(eventState)
                .build();
    }

    public static EventFullDto toEventFullDto(Event event, Long confirmedRequests, Long views) {

        return EventFullDto.builder()
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(confirmedRequests)
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .id(event.getId())
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .location(LocationDto.builder().lat(event.getLat()).lon(event.getLon()).build())
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(views)
                .build();
    }

    public static EventShortDto toEventShortDtoFromFlat(EventShortFlatDto eventShortFlatDto) {

        return EventShortDto.builder()
                .annotation(eventShortFlatDto.getAnnotation())
                .category(CategoryDto.builder().id(eventShortFlatDto.getCategoryId()).name(eventShortFlatDto.getCategoryName()).build())
                .confirmedRequests(eventShortFlatDto.getConfirmedRequests())
                .eventDate(eventShortFlatDto.getEventDate())
                .id(eventShortFlatDto.getId())
                .initiator(UserShortDto.builder().id(eventShortFlatDto.getInitiatorId()).name(eventShortFlatDto.getInitiatorName()).build())
                .paid(eventShortFlatDto.getPaid())
                .title(eventShortFlatDto.getTitle())
                .views(1L)
                .build();
    }

    public static EventFullDto toEventFullDtoFromFlat(EventFullFlatDto event, Long views) {

        return EventFullDto.builder()
                .annotation(event.getAnnotation())
                .category(CategoryDto.builder().id(event.getCategoryId()).name(event.getCategoryName()).build())
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .id(event.getId())
                .initiator(UserShortDto.builder().id(event.getInitiatorId()).name(event.getInitiatorName()).build())
                .location(LocationDto.builder().lat(event.getLat()).lon(event.getLon()).build())
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(views)
                .build();
    }

    public static Event toEventFromUpdateRequest(UpdateEventUserRequest updateEvent, Event event) {
        event.setAnnotation(Optional.ofNullable(updateEvent.getAnnotation()).orElse(event.getAnnotation()));
        event.setCategory(Optional.ofNullable(Category.builder().id(updateEvent.getCategory()).build()).orElse(event.getCategory()));
        event.setLon(Optional.ofNullable(updateEvent.getLocation() == null ? null : updateEvent.getLocation().getLon()).orElse(event.getLon()));
        event.setLat(Optional.ofNullable(updateEvent.getLocation() == null ? null : updateEvent.getLocation().getLat()).orElse(event.getLat()));
        event.setPaid(Optional.ofNullable(updateEvent.getPaid()).orElse(event.getPaid()));
        event.setParticipantLimit(Optional.ofNullable(updateEvent.getParticipantLimit()).orElse(event.getParticipantLimit()));
        event.setRequestModeration(Optional.ofNullable(updateEvent.getRequestModeration()).orElse(event.getRequestModeration()));
        event.setTitle(Optional.ofNullable(updateEvent.getTitle()).orElse(event.getTitle()));
        event.setEventDate(Optional.ofNullable(updateEvent.getEventDate()).orElse(event.getEventDate()));
        event.setDescription(Optional.ofNullable(updateEvent.getDescription()).orElse(event.getDescription()));

        return event;
    }

    public static Event toEventFromUpdateAdminRequest(UpdateEventAdminRequest updateEvent, Event event) {
        event.setAnnotation(Optional.ofNullable(updateEvent.getAnnotation()).orElse(event.getAnnotation()));
        event.setCategory(Optional.ofNullable(Category.builder().id(updateEvent.getCategory()).build()).orElse(event.getCategory()));
        event.setLon(Optional.ofNullable(updateEvent.getLocation() == null ? null : updateEvent.getLocation().getLon()).orElse(event.getLon()));
        event.setLat(Optional.ofNullable(updateEvent.getLocation() == null ? null : updateEvent.getLocation().getLat()).orElse(event.getLat()));
        event.setPaid(Optional.ofNullable(updateEvent.getPaid()).orElse(event.getPaid()));
        event.setParticipantLimit(Optional.ofNullable(updateEvent.getParticipantLimit()).orElse(event.getParticipantLimit()));
        event.setRequestModeration(Optional.ofNullable(updateEvent.getRequestModeration()).orElse(event.getRequestModeration()));
        event.setTitle(Optional.ofNullable(updateEvent.getTitle()).orElse(event.getTitle()));
        event.setDescription(Optional.ofNullable(updateEvent.getDescription()).orElse(event.getDescription()));

        return event;
    }
}
