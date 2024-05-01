package ru.practicum.ewm.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = {"id", "name"})
public class UserShortDto {

    private Long id;
    private String name;
}
