package com.api.reservavuelos.Utils;

import com.api.reservavuelos.annotations.ValidExpirationDate;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

public class ExpirationDateValidator implements ConstraintValidator<ValidExpirationDate, String> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy/MM");

    @Override
    public void initialize(ValidExpirationDate constraintAnnotation) {
    }

    @Override
    public boolean isValid(String expirationDate, ConstraintValidatorContext context) {
        if (expirationDate == null) {
            return false;
        }

        try {
            YearMonth date = YearMonth.parse(expirationDate, formatter);
            YearMonth currentDate = YearMonth.now();
            return date.isAfter(currentDate) || date.equals(currentDate);
        } catch (Exception e) {
            return false;
        }
    }
}
