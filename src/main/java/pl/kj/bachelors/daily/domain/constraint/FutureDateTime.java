package pl.kj.bachelors.daily.domain.constraint;

import pl.kj.bachelors.daily.domain.validator.FutureDateTimeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FutureDateTimeValidator.class)
@Target(value = ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FutureDateTime {
    String message() default "Invalid date!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
