package ru.practicum.ewm.model.request;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"email", "name"})
public class NewUserRequest {
    //Данные нового пользователя
    @NotBlank
    @Size(min = 6, max = 254)
    @Email
    private String email;

    @NotBlank
    @Size(min = 2, max = 250)
    private String name;
}
