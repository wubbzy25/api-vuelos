package com.api.reservavuelos.annotations;

import com.api.reservavuelos.Utils.EstadoVueloValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = EstadoVueloValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEstadoVuelo {
    String message() default "Estado de vuelo inválido";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
