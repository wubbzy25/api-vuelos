package com.api.reservavuelos.annotations;

import com.api.reservavuelos.Utils.CardNumberValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = CardNumberValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface CardNumberValidatorAnnotaion {
    String message() default "Número de tarjeta de crédito inválido";
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
