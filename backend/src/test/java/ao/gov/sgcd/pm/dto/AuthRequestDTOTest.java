package ao.gov.sgcd.pm.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class AuthRequestDTOTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }
    }

    @Test
    void noArgConstructor_shouldCreateEmptyDTO() {
        AuthRequestDTO dto = new AuthRequestDTO();

        assertNull(dto.getUsername());
        assertNull(dto.getPassword());
    }

    @Test
    void allArgsConstructor_shouldSetAllFields() {
        AuthRequestDTO dto = new AuthRequestDTO("admin", "admin123");

        assertEquals("admin", dto.getUsername());
        assertEquals("admin123", dto.getPassword());
    }

    @Test
    void gettersAndSetters_shouldWorkForAllFields() {
        AuthRequestDTO dto = new AuthRequestDTO();

        dto.setUsername("stakeholder");
        dto.setPassword("stakeholder2026");

        assertEquals("stakeholder", dto.getUsername());
        assertEquals("stakeholder2026", dto.getPassword());
    }

    @Test
    void equals_reflexive() {
        AuthRequestDTO dto = new AuthRequestDTO("admin", "admin123");
        assertEquals(dto, dto);
    }

    @Test
    void equals_symmetric() {
        AuthRequestDTO dto1 = new AuthRequestDTO("admin", "admin123");
        AuthRequestDTO dto2 = new AuthRequestDTO("admin", "admin123");

        assertEquals(dto1, dto2);
        assertEquals(dto2, dto1);
    }

    @Test
    void equals_nullReturnsFalse() {
        AuthRequestDTO dto = new AuthRequestDTO("admin", "admin123");
        assertNotEquals(null, dto);
    }

    @Test
    void equals_differentClassReturnsFalse() {
        AuthRequestDTO dto = new AuthRequestDTO("admin", "admin123");
        assertNotEquals("a string", dto);
    }

    @Test
    void equals_differentValuesReturnsFalse() {
        AuthRequestDTO dto1 = new AuthRequestDTO("admin", "admin123");
        AuthRequestDTO dto2 = new AuthRequestDTO("stakeholder", "stakeholder2026");

        assertNotEquals(dto1, dto2);
    }

    @Test
    void equals_differentUsernameReturnsFalse() {
        AuthRequestDTO dto1 = new AuthRequestDTO("admin", "admin123");
        AuthRequestDTO dto2 = new AuthRequestDTO("other", "admin123");

        assertNotEquals(dto1, dto2);
    }

    @Test
    void equals_differentPasswordReturnsFalse() {
        AuthRequestDTO dto1 = new AuthRequestDTO("admin", "admin123");
        AuthRequestDTO dto2 = new AuthRequestDTO("admin", "other123");

        assertNotEquals(dto1, dto2);
    }

    @Test
    void hashCode_equalObjectsSameHashCode() {
        AuthRequestDTO dto1 = new AuthRequestDTO("admin", "admin123");
        AuthRequestDTO dto2 = new AuthRequestDTO("admin", "admin123");

        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void hashCode_differentObjectsDifferentHashCode() {
        AuthRequestDTO dto1 = new AuthRequestDTO("admin", "admin123");
        AuthRequestDTO dto2 = new AuthRequestDTO("stakeholder", "stakeholder2026");

        assertNotEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void toString_containsClassNameAndFieldValues() {
        AuthRequestDTO dto = new AuthRequestDTO("admin", "admin123");

        String result = dto.toString();
        assertTrue(result.contains("AuthRequestDTO"));
        assertTrue(result.contains("admin"));
        assertTrue(result.contains("admin123"));
    }

    // --- Validation tests ---

    @Test
    void validation_validDTO_shouldHaveNoViolations() {
        AuthRequestDTO dto = new AuthRequestDTO("admin", "admin123");

        Set<ConstraintViolation<AuthRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void validation_nullUsername_shouldHaveViolation() {
        AuthRequestDTO dto = new AuthRequestDTO(null, "admin123");

        Set<ConstraintViolation<AuthRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("username")));
    }

    @Test
    void validation_emptyUsername_shouldHaveViolation() {
        AuthRequestDTO dto = new AuthRequestDTO("", "admin123");

        Set<ConstraintViolation<AuthRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("username")));
    }

    @Test
    void validation_blankUsername_shouldHaveViolation() {
        AuthRequestDTO dto = new AuthRequestDTO("   ", "admin123");

        Set<ConstraintViolation<AuthRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("username")));
    }

    @Test
    void validation_nullPassword_shouldHaveViolation() {
        AuthRequestDTO dto = new AuthRequestDTO("admin", null);

        Set<ConstraintViolation<AuthRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("password")));
    }

    @Test
    void validation_emptyPassword_shouldHaveViolation() {
        AuthRequestDTO dto = new AuthRequestDTO("admin", "");

        Set<ConstraintViolation<AuthRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("password")));
    }

    @Test
    void validation_blankPassword_shouldHaveViolation() {
        AuthRequestDTO dto = new AuthRequestDTO("admin", "   ");

        Set<ConstraintViolation<AuthRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("password")));
    }

    @Test
    void validation_bothFieldsNull_shouldHaveTwoViolations() {
        AuthRequestDTO dto = new AuthRequestDTO(null, null);

        Set<ConstraintViolation<AuthRequestDTO>> violations = validator.validate(dto);
        assertEquals(2, violations.size());
    }

    @Test
    void validation_bothFieldsBlank_shouldHaveTwoViolations() {
        AuthRequestDTO dto = new AuthRequestDTO("  ", "  ");

        Set<ConstraintViolation<AuthRequestDTO>> violations = validator.validate(dto);
        assertEquals(2, violations.size());
    }
}
