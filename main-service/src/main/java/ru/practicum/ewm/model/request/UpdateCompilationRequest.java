package ru.practicum.ewm.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateCompilationRequest {
    // Изменение информации о подборке событий. Если поле в запросе не указано (равно null)
    // - значит изменение этих данных не треубется.
    private List<Long> events;
    private Boolean pinned;
    private String title;
}
