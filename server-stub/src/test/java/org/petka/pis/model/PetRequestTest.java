package org.petka.pis.model;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import java.util.stream.Stream;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class PetRequestTest {

    private Validator validator;

    @BeforeEach
    public void init() {
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @ParameterizedTest
    @MethodSource("validationInput")
    void testValidation(String propertyName, String errorMessage) {
        PetRequest petRequest = new PetRequest();

        Set<ConstraintViolation<PetRequest>> violations = this.validator.validateProperty(petRequest, propertyName);
        assertNotNull(violations);
        assertTrue(violations.toString().contains(errorMessage), errorMessage);
    }

    @SuppressWarnings("unused")
    static Stream<Arguments> validationInput() {
        return Stream.of(
                Arguments.of("name", "must not be null"),
                Arguments.of("kind", "must not be null"),
                Arguments.of("age", "must not be null")
        );
    }
}
