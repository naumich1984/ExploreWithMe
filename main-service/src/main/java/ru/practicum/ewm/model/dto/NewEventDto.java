package ru.practicum.ewm.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NewEventDto {

    private String annotation;
    private Long category;
    private String description;
    private LocalDateTime eventDate;

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
    private String title;
}
