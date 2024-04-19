package ru.practicum.ewm.dto.stats;

import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatsDtoOut {
    private String app;
    private String uri;
    private Long hits;
}
