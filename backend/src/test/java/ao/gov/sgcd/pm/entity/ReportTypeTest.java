package ao.gov.sgcd.pm.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ReportTypeTest {

    @Test
    void values_shouldReturnAllValues() {
        assertEquals(3, ReportType.values().length);
    }

    @Test
    void valueOf_WEEKLY() {
        assertEquals(ReportType.WEEKLY, ReportType.valueOf("WEEKLY"));
    }

    @Test
    void valueOf_SPRINT_END() {
        assertEquals(ReportType.SPRINT_END, ReportType.valueOf("SPRINT_END"));
    }

    @Test
    void valueOf_CUSTOM() {
        assertEquals(ReportType.CUSTOM, ReportType.valueOf("CUSTOM"));
    }

    @Test
    void valueOf_invalid_shouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> ReportType.valueOf("INVALID"));
    }

    @Test
    void values_shouldContainAllExpectedValues() {
        ReportType[] values = ReportType.values();
        assertEquals(ReportType.WEEKLY, values[0]);
        assertEquals(ReportType.SPRINT_END, values[1]);
        assertEquals(ReportType.CUSTOM, values[2]);
    }

    @Test
    void ordinal_shouldMatchDeclarationOrder() {
        assertEquals(0, ReportType.WEEKLY.ordinal());
        assertEquals(1, ReportType.SPRINT_END.ordinal());
        assertEquals(2, ReportType.CUSTOM.ordinal());
    }

    @Test
    void name_shouldReturnStringRepresentation() {
        assertEquals("WEEKLY", ReportType.WEEKLY.name());
        assertEquals("SPRINT_END", ReportType.SPRINT_END.name());
        assertEquals("CUSTOM", ReportType.CUSTOM.name());
    }
}
