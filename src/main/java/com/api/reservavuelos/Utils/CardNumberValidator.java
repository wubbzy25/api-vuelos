package com.api.reservavuelos.Utils;

import com.api.reservavuelos.annotations.CardNumberValidatorAnnotaion;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class CardNumberValidator implements ConstraintValidator<CardNumberValidatorAnnotaion, String> {

    private static final String VISA_REGEX = "^4[0-9]{12}(?:[0-9]{3})?$";
    private static final String MASTERCARD_REGEX = "^5[1-5][0-9]{14}$";
    private static final String AMEX_REGEX = "^3[47][0-9]{13}$";
    private static final String DINERS_REGEX = "^3(?:0[0-5]|[68][0-9])[0-9]{11}$";
    private static final String DISCOVER_REGEX = "^6(?:011|5[0-9]{2})[0-9]{12}$";
    private static final String JCB_REGEX = "^(?:2131|1800|35\\d{3})\\d{11}$";

    private static final Pattern VISA_PATTERN = Pattern.compile(VISA_REGEX);
    private static final Pattern MASTERCARD_PATTERN = Pattern.compile(MASTERCARD_REGEX);
    private static final Pattern AMEX_PATTERN = Pattern.compile(AMEX_REGEX);
    private static final Pattern DINERS_PATTERN = Pattern.compile(DINERS_REGEX);
    private static final Pattern DISCOVER_PATTERN = Pattern.compile(DISCOVER_REGEX);
    private static final Pattern JCB_PATTERN = Pattern.compile(JCB_REGEX);

    @Override
    public void initialize(CardNumberValidatorAnnotaion constraintAnnotation) {
    }

    @Override
    public boolean isValid(String creditCardNumber, ConstraintValidatorContext context) {
        if (creditCardNumber == null) {
            return false;
        }

        return VISA_PATTERN.matcher(creditCardNumber).matches()
                || MASTERCARD_PATTERN.matcher(creditCardNumber).matches()
                || AMEX_PATTERN.matcher(creditCardNumber).matches()
                || DINERS_PATTERN.matcher(creditCardNumber).matches()
                || DISCOVER_PATTERN.matcher(creditCardNumber).matches()
                || JCB_PATTERN.matcher(creditCardNumber).matches();
    }
}
