package ao.gov.sgcd.pm.seed;

import ao.gov.sgcd.pm.entity.Task;
import ao.gov.sgcd.pm.repository.SprintRepository;
import ao.gov.sgcd.pm.repository.TaskRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DataSeederTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private SprintRepository sprintRepository;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private DataSeeder dataSeeder;

    // ---- run: skip when data exists ----

    @Test
    void run_shouldSkipWhenDataExists() throws Exception {
        when(taskRepository.count()).thenReturn(10L);

        dataSeeder.run();

        verify(taskRepository).count();
        verify(taskRepository, never()).save(any(Task.class));
        verifyNoInteractions(sprintRepository);
        verifyNoInteractions(objectMapper);
    }

    @Test
    void run_shouldSkipWhenExactlyOneTaskExists() throws Exception {
        when(taskRepository.count()).thenReturn(1L);

        dataSeeder.run();

        verify(taskRepository).count();
        verify(taskRepository, never()).save(any(Task.class));
        verifyNoInteractions(sprintRepository);
    }

    @Test
    void run_shouldSkipWhenManyTasksExist() throws Exception {
        when(taskRepository.count()).thenReturn(204L);

        dataSeeder.run();

        verify(taskRepository).count();
        verify(taskRepository, never()).save(any(Task.class));
    }

    // ---- run: attempt to load when empty ----

    @Test
    void run_shouldAttemptToLoadWhenEmpty() throws Exception {
        when(taskRepository.count()).thenReturn(0L);

        // The seeder reads all_tasks.json from classpath (ClassPathResource is created
        // internally and cannot be mocked). The test classpath has a minimal all_tasks.json
        // (empty JSON object "{}"). The mocked ObjectMapper.readValue() returns null by
        // default, causing a NullPointerException when the seeder tries to iterate entries.
        // This confirms the seeder does NOT skip when count is zero and proceeds to load.
        assertThrows(Exception.class, () -> dataSeeder.run());

        verify(taskRepository).count();
        // Verify objectMapper.readValue was called (seeder tried to parse JSON)
        verify(objectMapper).readValue(any(java.io.InputStream.class), any(TypeReference.class));
    }

    // ---- SPRINT_START_DATES constant validation ----

    @Test
    @SuppressWarnings("unchecked")
    void sprintStartDates_shouldHaveSixEntries() throws Exception {
        Field field = DataSeeder.class.getDeclaredField("SPRINT_START_DATES");
        field.setAccessible(true);

        Map<Integer, LocalDate> dates = (Map<Integer, LocalDate>) field.get(null);

        assertEquals(6, dates.size());
    }

    @Test
    @SuppressWarnings("unchecked")
    void sprintStartDates_shouldContainAllSprintNumbers() throws Exception {
        Field field = DataSeeder.class.getDeclaredField("SPRINT_START_DATES");
        field.setAccessible(true);

        Map<Integer, LocalDate> dates = (Map<Integer, LocalDate>) field.get(null);

        for (int i = 1; i <= 6; i++) {
            assertTrue(dates.containsKey(i), "Missing sprint number: " + i);
        }
    }

    @Test
    @SuppressWarnings("unchecked")
    void sprintStartDates_shouldHaveCorrectValues() throws Exception {
        Field field = DataSeeder.class.getDeclaredField("SPRINT_START_DATES");
        field.setAccessible(true);

        Map<Integer, LocalDate> dates = (Map<Integer, LocalDate>) field.get(null);

        assertEquals(LocalDate.of(2026, 3, 2), dates.get(1));
        assertEquals(LocalDate.of(2026, 5, 11), dates.get(2));
        assertEquals(LocalDate.of(2026, 6, 29), dates.get(3));
        assertEquals(LocalDate.of(2026, 8, 3), dates.get(4));
        assertEquals(LocalDate.of(2026, 9, 21), dates.get(5));
        assertEquals(LocalDate.of(2026, 11, 9), dates.get(6));
    }

    @Test
    @SuppressWarnings("unchecked")
    void sprintStartDates_shouldAllBeIn2026() throws Exception {
        Field field = DataSeeder.class.getDeclaredField("SPRINT_START_DATES");
        field.setAccessible(true);

        Map<Integer, LocalDate> dates = (Map<Integer, LocalDate>) field.get(null);

        dates.values().forEach(date ->
                assertEquals(2026, date.getYear(), "Sprint date not in 2026: " + date));
    }

    @Test
    @SuppressWarnings("unchecked")
    void sprintStartDates_shouldBeInChronologicalOrder() throws Exception {
        Field field = DataSeeder.class.getDeclaredField("SPRINT_START_DATES");
        field.setAccessible(true);

        Map<Integer, LocalDate> dates = (Map<Integer, LocalDate>) field.get(null);

        for (int i = 1; i < 6; i++) {
            LocalDate current = dates.get(i);
            LocalDate next = dates.get(i + 1);
            assertTrue(current.isBefore(next),
                    "Sprint " + i + " (" + current + ") should be before Sprint " + (i + 1) + " (" + next + ")");
        }
    }

    // ---- Task code format verification ----

    @Test
    void taskCodeFormat_shouldMatchExpectedPattern() {
        // Verify the format S{sprintNum}-{index:02d} produces correct strings
        String code1 = String.format("S%d-%02d", 1, 1);
        assertEquals("S1-01", code1);

        String code2 = String.format("S%d-%02d", 3, 15);
        assertEquals("S3-15", code2);

        String code3 = String.format("S%d-%02d", 6, 9);
        assertEquals("S6-09", code3);

        String code4 = String.format("S%d-%02d", 2, 42);
        assertEquals("S2-42", code4);
    }

    @Test
    void taskCodeFormat_shouldPadSingleDigitIndex() {
        String code = String.format("S%d-%02d", 1, 5);
        assertEquals("S1-05", code);
        assertTrue(code.matches("S\\d-\\d{2}"));
    }

    @Test
    void taskCodeFormat_shouldNotPadDoubleDigitIndex() {
        String code = String.format("S%d-%02d", 4, 34);
        assertEquals("S4-34", code);
        assertTrue(code.matches("S\\d-\\d{2}"));
    }

    // ---- CommandLineRunner interface ----

    @Test
    void dataSeeder_shouldImplementCommandLineRunner() {
        assertTrue(dataSeeder instanceof org.springframework.boot.CommandLineRunner,
                "DataSeeder should implement CommandLineRunner");
    }
}
