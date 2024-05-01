package ru.practicum.ewm.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime created;
    private Long requester;
    private Long eventId;
    private EventState eventState;
    private Integer participantLimit;
    private Boolean requestModeration;

}
