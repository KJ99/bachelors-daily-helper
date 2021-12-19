package pl.kj.bachelors.daily.domain.validator;

import pl.kj.bachelors.daily.domain.constraint.FutureDateTime;
import pl.kj.bachelors.daily.infrastructure.user.RequestHolder;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class FutureDateTimeValidator implements ConstraintValidator<FutureDateTime, Object> {
    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        boolean isValid;
        try {
            Date date = format.parse((String) o);
            TimeZone timeZone = RequestHolder.getRequestTimeZone().orElse(TimeZone.getDefault());
            isValid = date.getTime() > Calendar.getInstance(timeZone).getTimeInMillis();
        } catch (ParseException e) {
            isValid = false;
        }

        return isValid;
    }
}
