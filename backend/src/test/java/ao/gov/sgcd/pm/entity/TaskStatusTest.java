package ao.gov.sgcd.pm.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskStatusTest {

    @Test
    void values_shouldReturnAllValues() {
        assertEquals(5, TaskStatus.values().length);
    }

    @Test
    void valueOf_PLANNED() {
        assertEquals(TaskStatus.PLANNED, TaskStatus.valueOf("PLANNED"));
    }

    @Test
    void valueOf_IN_PROGRESS() {
        assertEquals(TaskStatus.IN_PROGRESS, TaskStatus.valueOf("IN_PROGRESS"));
    }

    @Test
    void valueOf_COMPLETED() {
        assertEquals(TaskStatus.COMPLETED, TaskStatus.valueOf("COMPLETED"));
    }

    @Test
    void valueOf_BLOCKED() {
        assertEquals(TaskStatus.BLOCKED, TaskStatus.valueOf("BLOCKED"));
    }

    @Test
    void valueOf_SKIPPED() {
        assertEquals(TaskStatus.SKIPPED, TaskStatus.valueOf("SKIPPED"));
    }

    @Test
    void valueOf_invalid_shouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> TaskStatus.valueOf("INVALID"));
    }

    @Test
    void values_shouldContainAllExpectedValues() {
        TaskStatus[] values = TaskStatus.values();
        assertEquals(TaskStatus.PLANNED, values[0]);
        assertEquals(TaskStatus.IN_PROGRESS, values[1]);
        assertEquals(TaskStatus.COMPLETED, values[2]);
        assertEquals(TaskStatus.BLOCKED, values[3]);
        assertEquals(TaskStatus.SKIPPED, values[4]);
    }

    @Test
    void ordinal_shouldMatchDeclarationOrder() {
        assertEquals(0, TaskStatus.PLANNED.ordinal());
        assertEquals(1, TaskStatus.IN_PROGRESS.ordinal());
        assertEquals(2, TaskStatus.COMPLETED.ordinal());
        assertEquals(3, TaskStatus.BLOCKED.ordinal());
        assertEquals(4, TaskStatus.SKIPPED.ordinal());
    }

    @Test
    void name_shouldReturnStringRepresentation() {
        assertEquals("PLANNED", TaskStatus.PLANNED.name());
        assertEquals("IN_PROGRESS", TaskStatus.IN_PROGRESS.name());
        assertEquals("COMPLETED", TaskStatus.COMPLETED.name());
        assertEquals("BLOCKED", TaskStatus.BLOCKED.name());
        assertEquals("SKIPPED", TaskStatus.SKIPPED.name());
    }
}
