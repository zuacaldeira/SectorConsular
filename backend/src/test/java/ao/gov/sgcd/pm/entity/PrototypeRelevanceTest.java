package ao.gov.sgcd.pm.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PrototypeRelevanceTest {

    @Test
    void values_shouldReturnAllValues() {
        assertEquals(3, PrototypeRelevance.values().length);
    }

    @Test
    void valueOf_primary() {
        assertEquals(PrototypeRelevance.primary, PrototypeRelevance.valueOf("primary"));
    }

    @Test
    void valueOf_reference() {
        assertEquals(PrototypeRelevance.reference, PrototypeRelevance.valueOf("reference"));
    }

    @Test
    void valueOf_inspiration() {
        assertEquals(PrototypeRelevance.inspiration, PrototypeRelevance.valueOf("inspiration"));
    }

    @Test
    void valueOf_invalid_shouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> PrototypeRelevance.valueOf("INVALID"));
    }

    @Test
    void valueOf_uppercasePrimary_shouldThrow() {
        // Values are lowercase, so uppercase should fail
        assertThrows(IllegalArgumentException.class, () -> PrototypeRelevance.valueOf("PRIMARY"));
    }

    @Test
    void values_shouldContainAllExpectedValues() {
        PrototypeRelevance[] values = PrototypeRelevance.values();
        assertEquals(PrototypeRelevance.primary, values[0]);
        assertEquals(PrototypeRelevance.reference, values[1]);
        assertEquals(PrototypeRelevance.inspiration, values[2]);
    }

    @Test
    void ordinal_shouldMatchDeclarationOrder() {
        assertEquals(0, PrototypeRelevance.primary.ordinal());
        assertEquals(1, PrototypeRelevance.reference.ordinal());
        assertEquals(2, PrototypeRelevance.inspiration.ordinal());
    }

    @Test
    void name_shouldReturnLowercaseStringRepresentation() {
        assertEquals("primary", PrototypeRelevance.primary.name());
        assertEquals("reference", PrototypeRelevance.reference.name());
        assertEquals("inspiration", PrototypeRelevance.inspiration.name());
    }
}
