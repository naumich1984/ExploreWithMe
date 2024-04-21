package ru.practicum.ewm.model.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id", "name", "email"})
public class UserDto {

    private String email;
    private Long id;
    private String name;
}
