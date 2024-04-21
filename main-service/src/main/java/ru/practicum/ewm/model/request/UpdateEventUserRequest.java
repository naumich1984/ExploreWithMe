package ru.practicum.ewm.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.ewm.model._enum.EventUpdateState;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventUserRequest {
    //Данные для изменения информации о событии. Если поле в запросе не указано (равно null)
    // - значит изменение этих данных не треубется.
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
    private EventUpdateState stateAction;
    private String title;
}
