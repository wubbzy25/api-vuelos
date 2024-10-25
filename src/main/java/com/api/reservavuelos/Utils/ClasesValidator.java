package com.api.reservavuelos.Utils;

import com.api.reservavuelos.annotations.ValidClase;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ClasesValidator implements ConstraintValidator<ValidClase, Clases> {
    @Override
    public boolean isValid(Clases clases, ConstraintValidatorContext constraintValidatorContext) {
        if (clases == null){
            return false;
        }
        try {
            Clases.valueOf(clases.name());
            return true;
        } catch (IllegalArgumentException e){
            return false;
        }
    }
}
