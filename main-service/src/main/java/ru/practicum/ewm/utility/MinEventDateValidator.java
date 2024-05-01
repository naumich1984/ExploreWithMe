package ru.practicum.ewm.utility;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDateTime;

public class MinEventDateValidator implements ConstraintValidator<MinEventDate, LocalDateTime> {

    private int hours;

    @Override
    public void initialize(MinEventDate constraintAnnotation) {
        hours = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(LocalDateTime value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        LocalDateTime nowPlus2Hours = LocalDateTime.now().plusHours(hours);

        return value.isAfter(nowPlus2Hours);
    }
}
