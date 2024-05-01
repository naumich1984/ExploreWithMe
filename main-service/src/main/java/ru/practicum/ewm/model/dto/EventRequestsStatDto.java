package ru.practicum.ewm.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.ewm.model._enum.RequestStatus;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestsStatDto {

    Integer participantLimit;
    Boolean requestModeration;

    @Enumerated(EnumType.STRING)
    RequestStatus requestStatus;
    Long countRequests;
}
