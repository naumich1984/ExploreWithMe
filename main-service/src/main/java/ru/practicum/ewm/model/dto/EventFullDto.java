package ru.practicum.ewm.model.dto;

import lombok.*;
import ru.practicum.ewm.model._enum.EventState;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id", "title", "eventDate"})
public class EventFullDto {

    private String annotation;
    private CategoryDto category;
    private Long confirmedRequests;
    private LocalDateTime createdOn;
    private String description;
    private LocalDateTime eventDate;
    private Long id;
    private UserShortDto initiator;

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Location {
        private Float lat;
        private Float lon;
    }

    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private EventState state;
    private String title;
    private Long views;
}
