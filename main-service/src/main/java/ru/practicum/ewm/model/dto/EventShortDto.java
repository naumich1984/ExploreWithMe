package ru.practicum.ewm.model.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class EventShortDto {

    private String annotation;
    private CategoryDto category;
    private LocalDateTime eventDate;
    private Long id;
    private UserShortDto initiator;
    private Boolean paid;
    private String title;
    private Long views;
    private Long confirmedRequests;
}
