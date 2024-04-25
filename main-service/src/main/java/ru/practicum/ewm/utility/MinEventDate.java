package ru.practicum.ewm.utility;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Constraint(validatedBy = MinEventDateValidator.class)
@Target(FIELD)
@Retention(RUNTIME)
@Documented
public @interface MinEventDate {
    String message() default "Time must be at least 2 hours in the future";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
