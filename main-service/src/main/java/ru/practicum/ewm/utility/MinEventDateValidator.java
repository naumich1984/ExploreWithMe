package ru.practicum.ewm.utility;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class MinEventDateValidator implements ConstraintValidator<MinEventDate, LocalDateTime> {

    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        LocalDateTime nowPlus2Hours = LocalDateTime.now().plusHours(2);

        return value.isAfter(nowPlus2Hours);
    }
}
