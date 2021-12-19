package pl.kj.bachelors.daily.domain.validator;

import pl.kj.bachelors.daily.domain.constraint.FromEnum;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

public class FromEnumValidator implements ConstraintValidator<FromEnum, Object> {
    private List<String> acceptedValues = null;

    @Override
    public void initialize(FromEnum constraintAnnotation) {
        this.acceptedValues = new ArrayList<>();
        Class<? extends Enum<?>> enumClass = constraintAnnotation.enumClass();
        var fields = enumClass.getEnumConstants();
        for(var field : fields) {
            this.acceptedValues.add(field.name());
        }
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        return o == null || this.acceptedValues.contains((String) o);
    }
}
