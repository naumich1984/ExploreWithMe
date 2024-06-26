package ru.practicum.ewm.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id", "name"})
@Builder
public class CategoryDto {

    private Long id;
    private String name;
}
