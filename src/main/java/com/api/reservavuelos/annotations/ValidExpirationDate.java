package com.api.reservavuelos.annotations;

import com.api.reservavuelos.Utils.ExpirationDateValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ExpirationDateValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidExpirationDate {
    String message() default "La fecha de expiración no debe haber pasado";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}