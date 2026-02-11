package ao.gov.sgcd.pm.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskNoteTest {

    @Test
    void builder_shouldCreateTaskNoteWithAllFields() {
        Task task = new Task();
        LocalDateTime createdAt = LocalDateTime.now();

        TaskNote note = TaskNote.builder()
                .id(1L)
                .task(task)
                .noteType(NoteType.WARNING)
                .content("Performance issue detected")
                .author("senior-dev")
                .createdAt(createdAt)
                .build();

        assertEquals(1L, note.getId());
        assertSame(task, note.getTask());
        assertEquals(NoteType.WARNING, note.getNoteType());
        assertEquals("Performance issue detected", note.getContent());
        assertEquals("senior-dev", note.getAuthor());
        assertEquals(createdAt, note.getCreatedAt());
    }

    @Test
    void gettersAndSetters_shouldWorkForAllFields() {
        TaskNote note = new TaskNote();
        Task task = new Task();
        LocalDateTime createdAt = LocalDateTime.now();

        note.setId(2L);
        note.setTask(task);
        note.setNoteType(NoteType.BLOCKER);
        note.setContent("Database connection refused");
        note.setAuthor("ops-team");
        note.setCreatedAt(createdAt);

        assertEquals(2L, note.getId());
        assertSame(task, note.getTask());
        assertEquals(NoteType.BLOCKER, note.getNoteType());
        assertEquals("Database connection refused", note.getContent());
        assertEquals("ops-team", note.getAuthor());
        assertEquals(createdAt, note.getCreatedAt());
    }

    @Test
    void noArgConstructor_shouldCreateEmptyTaskNote() {
        TaskNote note = new TaskNote();

        assertNull(note.getId());
        assertNull(note.getTask());
        assertNull(note.getNoteType());
        assertNull(note.getContent());
        assertNull(note.getAuthor());
        assertNull(note.getCreatedAt());
    }

    @Test
    void allArgsConstructor_shouldSetAllFields() {
        Task task = new Task();
        LocalDateTime createdAt = LocalDateTime.now();

        TaskNote note = new TaskNote(
                3L, task, NoteType.DECISION,
                "Use MySQL instead of PostgreSQL",
                "architect", createdAt
        );

        assertEquals(3L, note.getId());
        assertSame(task, note.getTask());
        assertEquals(NoteType.DECISION, note.getNoteType());
        assertEquals("Use MySQL instead of PostgreSQL", note.getContent());
        assertEquals("architect", note.getAuthor());
        assertEquals(createdAt, note.getCreatedAt());
    }

    @Test
    void prePersist_shouldSetCreatedAt() {
        TaskNote note = new TaskNote();
        assertNull(note.getCreatedAt());

        note.onCreate();

        assertNotNull(note.getCreatedAt());
    }

    @Test
    void prePersist_shouldSetDefaultNoteType() {
        TaskNote note = new TaskNote();
        assertNull(note.getNoteType());

        note.onCreate();

        assertEquals(NoteType.INFO, note.getNoteType());
    }

    @Test
    void prePersist_shouldSetDefaultAuthor() {
        TaskNote note = new TaskNote();
        assertNull(note.getAuthor());

        note.onCreate();

        assertEquals("developer", note.getAuthor());
    }

    @Test
    void prePersist_shouldNotOverrideExistingNoteType() {
        TaskNote note = new TaskNote();
        note.setNoteType(NoteType.BLOCKER);

        note.onCreate();

        assertEquals(NoteType.BLOCKER, note.getNoteType());
    }

    @Test
    void prePersist_shouldNotOverrideExistingAuthor() {
        TaskNote note = new TaskNote();
        note.setAuthor("custom-author");

        note.onCreate();

        assertEquals("custom-author", note.getAuthor());
    }

    @Test
    void prePersist_createdAtShouldBeCurrentTime() {
        TaskNote note = new TaskNote();
        LocalDateTime before = LocalDateTime.now();

        note.onCreate();

        LocalDateTime after = LocalDateTime.now();
        assertNotNull(note.getCreatedAt());
        assertFalse(note.getCreatedAt().isBefore(before));
        assertFalse(note.getCreatedAt().isAfter(after));
    }
}
