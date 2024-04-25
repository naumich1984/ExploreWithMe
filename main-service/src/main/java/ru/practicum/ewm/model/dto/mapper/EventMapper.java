package ru.practicum.ewm.model.dto.mapper;

import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.User;
import ru.practicum.ewm.model._enum.EventState;
import ru.practicum.ewm.model.dto.*;

public class EventMapper {

    public static Event toEvent(NewEventDto newEventDto, Long userId, EventState eventState) {

        return Event.builder()
                .annotation(newEventDto.getAnnotation())
                .category(Category.builder().id(newEventDto.getCategory()).build())
                .description(newEventDto.getDescription())
                .eventDate(newEventDto.getEventDate())
                .lat(newEventDto.getLocation().getLat())
                .lon(newEventDto.getLocation().getLon())
                .paid(newEventDto.getPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.getRequestModeration())
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
                .views(eventShortFlatDto.getViews())
                .build();
    }

}
