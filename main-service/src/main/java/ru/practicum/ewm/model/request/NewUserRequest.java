package ru.practicum.ewm.model.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"email", "name"})
public class NewUserRequest {
    //Данные нового пользователя
    private String email;
    private String name;
}
