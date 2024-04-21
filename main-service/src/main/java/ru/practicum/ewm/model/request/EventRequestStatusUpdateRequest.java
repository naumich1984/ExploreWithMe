package ru.practicum.ewm.model.request;

import lombok.*;
import ru.practicum.ewm.model._enum.RequestStatus;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestStatusUpdateRequest {
    //Изменение статуса запроса на участие в событии текущего пользователя
    private List<Long> requestIds;
    private RequestStatus status;
}
