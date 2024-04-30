package ru.practicum.ewm.model.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.ewm.model._enum.EventUpdateState;
import ru.practicum.ewm.model.dto.LocationDto;
import ru.practicum.ewm.utility.MinEventDate;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEventUserRequest {
    //Данные для изменения информации о событии. Если поле в запросе не указано (равно null)
    // - значит изменение этих данных не треубется.
    @Size(min = 20, max = 2000)
    private String annotation;
    private Long category;

    @Size(min = 20, max = 7000)
    private String description;

    @MinEventDate(2)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime eventDate;
    private LocationDto location;
    private Boolean paid;

    @Min(0)
    private Integer participantLimit;
    private Boolean requestModeration;

    @Enumerated(EnumType.STRING)
    private EventUpdateState stateAction;

    @Size(min = 3, max = 120)
    private String title;
}
