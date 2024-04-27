package ru.practicum.ewm.model.dto;

import lombok.*;
import ru.practicum.ewm.model._enum.EventState;
import ru.practicum.ewm.model._enum.RequestStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestsConfirmDto {
    private Long requestId;
    private RequestStatus requestStatus;
    private LocalDateTime created;
    private Long requester;
    private Long eventId;
    private EventState eventState;
    private Integer participantLimit;
    private Boolean requestModeration;

}
