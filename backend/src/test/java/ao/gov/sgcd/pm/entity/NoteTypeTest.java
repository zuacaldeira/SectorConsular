package ao.gov.sgcd.pm.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NoteTypeTest {

    @Test
    void values_shouldReturnAllValues() {
        assertEquals(5, NoteType.values().length);
    }

    @Test
    void valueOf_INFO() {
        assertEquals(NoteType.INFO, NoteType.valueOf("INFO"));
    }

    @Test
    void valueOf_WARNING() {
        assertEquals(NoteType.WARNING, NoteType.valueOf("WARNING"));
    }

    @Test
    void valueOf_BLOCKER() {
        assertEquals(NoteType.BLOCKER, NoteType.valueOf("BLOCKER"));
    }

    @Test
    void valueOf_DECISION() {
        assertEquals(NoteType.DECISION, NoteType.valueOf("DECISION"));
    }

    @Test
    void valueOf_OBSERVATION() {
        assertEquals(NoteType.OBSERVATION, NoteType.valueOf("OBSERVATION"));
    }

    @Test
    void valueOf_invalid_shouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> NoteType.valueOf("INVALID"));
    }

    @Test
    void values_shouldContainAllExpectedValues() {
        NoteType[] values = NoteType.values();
        assertEquals(NoteType.INFO, values[0]);
        assertEquals(NoteType.WARNING, values[1]);
        assertEquals(NoteType.BLOCKER, values[2]);
        assertEquals(NoteType.DECISION, values[3]);
        assertEquals(NoteType.OBSERVATION, values[4]);
    }

    @Test
    void ordinal_shouldMatchDeclarationOrder() {
        assertEquals(0, NoteType.INFO.ordinal());
        assertEquals(1, NoteType.WARNING.ordinal());
        assertEquals(2, NoteType.BLOCKER.ordinal());
        assertEquals(3, NoteType.DECISION.ordinal());
        assertEquals(4, NoteType.OBSERVATION.ordinal());
    }

    @Test
    void name_shouldReturnStringRepresentation() {
        assertEquals("INFO", NoteType.INFO.name());
        assertEquals("WARNING", NoteType.WARNING.name());
        assertEquals("BLOCKER", NoteType.BLOCKER.name());
        assertEquals("DECISION", NoteType.DECISION.name());
        assertEquals("OBSERVATION", NoteType.OBSERVATION.name());
    }
}
