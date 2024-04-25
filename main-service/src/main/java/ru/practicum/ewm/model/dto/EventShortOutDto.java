package ru.practicum.ewm.model.dto;

import lombok.*;

import javax.persistence.Entity;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class EventShortOutDto {

    private String annotation;
    private Long categoryId;
    private String categoryName;
    private LocalDateTime eventDate;
    private Long id;
    private Long initiatorId;
    private Long initiatorName;
    private Boolean paid;
    private String title;
    private Long confirmedRequests;

}

