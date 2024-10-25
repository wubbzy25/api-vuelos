package com.api.reservavuelos.Utils;

import com.api.reservavuelos.annotations.ValidEstadoVuelo;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EstadoVueloValidator implements ConstraintValidator<ValidEstadoVuelo, VueloEstado> {
    @Override
    public boolean isValid(VueloEstado vueloEstado, ConstraintValidatorContext constraintValidatorContext) {
        if (vueloEstado == null) {
            return false;
        }
        try {
            VueloEstado.valueOf(vueloEstado.name());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
