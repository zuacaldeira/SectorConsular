package ao.gov.sgcd.pm.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SprintStatusTest {

    @Test
    void values_shouldReturnAllValues() {
        assertEquals(3, SprintStatus.values().length);
    }

    @Test
    void valueOf_PLANNED() {
        assertEquals(SprintStatus.PLANNED, SprintStatus.valueOf("PLANNED"));
    }

    @Test
    void valueOf_ACTIVE() {
        assertEquals(SprintStatus.ACTIVE, SprintStatus.valueOf("ACTIVE"));
    }

    @Test
    void valueOf_COMPLETED() {
        assertEquals(SprintStatus.COMPLETED, SprintStatus.valueOf("COMPLETED"));
    }

    @Test
    void valueOf_invalid_shouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> SprintStatus.valueOf("INVALID"));
    }

    @Test
    void values_shouldContainAllExpectedValues() {
        SprintStatus[] values = SprintStatus.values();
        assertEquals(SprintStatus.PLANNED, values[0]);
        assertEquals(SprintStatus.ACTIVE, values[1]);
        assertEquals(SprintStatus.COMPLETED, values[2]);
    }

    @Test
    void ordinal_shouldMatchDeclarationOrder() {
        assertEquals(0, SprintStatus.PLANNED.ordinal());
        assertEquals(1, SprintStatus.ACTIVE.ordinal());
        assertEquals(2, SprintStatus.COMPLETED.ordinal());
    }

    @Test
    void name_shouldReturnStringRepresentation() {
        assertEquals("PLANNED", SprintStatus.PLANNED.name());
        assertEquals("ACTIVE", SprintStatus.ACTIVE.name());
        assertEquals("COMPLETED", SprintStatus.COMPLETED.name());
    }
}
