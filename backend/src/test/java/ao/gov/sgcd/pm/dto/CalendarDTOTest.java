package ao.gov.sgcd.pm.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CalendarDTOTest {

    // --- CalendarDTO tests ---

    @Test
    void builder_shouldCreateDTOWithAllFields() {
        List<CalendarDTO.CalendarDayDTO> days = new ArrayList<>();

        CalendarDTO dto = CalendarDTO.builder()
                .year(2026)
                .month(1)
                .days(days)
                .build();

        assertEquals(2026, dto.getYear());
        assertEquals(1, dto.getMonth());
        assertSame(days, dto.getDays());
    }

    @Test
    void noArgConstructor_shouldCreateEmptyDTO() {
        CalendarDTO dto = new CalendarDTO();

        assertNull(dto.getYear());
        assertNull(dto.getMonth());
        assertNull(dto.getDays());
    }

    @Test
    void allArgsConstructor_shouldSetAllFields() {
        List<CalendarDTO.CalendarDayDTO> days = new ArrayList<>();

        CalendarDTO dto = new CalendarDTO(2026, 2, days);

        assertEquals(2026, dto.getYear());
        assertEquals(2, dto.getMonth());
        assertSame(days, dto.getDays());
    }

    @Test
    void gettersAndSetters_shouldWorkForAllFields() {
        CalendarDTO dto = new CalendarDTO();
        List<CalendarDTO.CalendarDayDTO> days = new ArrayList<>();

        dto.setYear(2026);
        dto.setMonth(3);
        dto.setDays(days);

        assertEquals(2026, dto.getYear());
        assertEquals(3, dto.getMonth());
        assertSame(days, dto.getDays());
    }

    @Test
    void equals_reflexive() {
        CalendarDTO dto = CalendarDTO.builder().year(2026).month(1).build();
        assertEquals(dto, dto);
    }

    @Test
    void equals_symmetric() {
        CalendarDTO dto1 = CalendarDTO.builder().year(2026).month(1).build();
        CalendarDTO dto2 = CalendarDTO.builder().year(2026).month(1).build();

        assertEquals(dto1, dto2);
        assertEquals(dto2, dto1);
    }

    @Test
    void equals_nullReturnsFalse() {
        CalendarDTO dto = CalendarDTO.builder().year(2026).build();
        assertNotEquals(null, dto);
    }

    @Test
    void equals_differentClassReturnsFalse() {
        CalendarDTO dto = CalendarDTO.builder().year(2026).build();
        assertNotEquals("a string", dto);
    }

    @Test
    void equals_differentValuesReturnsFalse() {
        CalendarDTO dto1 = CalendarDTO.builder().year(2026).month(1).build();
        CalendarDTO dto2 = CalendarDTO.builder().year(2026).month(2).build();

        assertNotEquals(dto1, dto2);
    }

    @Test
    void hashCode_equalObjectsSameHashCode() {
        CalendarDTO dto1 = CalendarDTO.builder().year(2026).month(1).build();
        CalendarDTO dto2 = CalendarDTO.builder().year(2026).month(1).build();

        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void hashCode_differentObjectsDifferentHashCode() {
        CalendarDTO dto1 = CalendarDTO.builder().year(2026).month(1).build();
        CalendarDTO dto2 = CalendarDTO.builder().year(2026).month(6).build();

        assertNotEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void toString_containsClassNameAndFieldValues() {
        CalendarDTO dto = CalendarDTO.builder()
                .year(2026)
                .month(1)
                .build();

        String result = dto.toString();
        assertTrue(result.contains("CalendarDTO"));
        assertTrue(result.contains("2026"));
        assertTrue(result.contains("1"));
    }

    // --- CalendarDayDTO tests ---

    @Test
    void calendarDayDTO_builder_shouldCreateWithAllFields() {
        LocalDate date = LocalDate.of(2026, 1, 6);
        TaskDTO task = TaskDTO.builder().id(1L).taskCode("S1-D01").build();

        CalendarDTO.CalendarDayDTO dto = CalendarDTO.CalendarDayDTO.builder()
                .date(date)
                .dayOfWeek("MONDAY")
                .isBlocked(false)
                .blockReason(null)
                .task(task)
                .isWorkDay(true)
                .build();

        assertEquals(date, dto.getDate());
        assertEquals("MONDAY", dto.getDayOfWeek());
        assertFalse(dto.isBlocked());
        assertNull(dto.getBlockReason());
        assertSame(task, dto.getTask());
        assertTrue(dto.isWorkDay());
    }

    @Test
    void calendarDayDTO_noArgConstructor_shouldCreateEmpty() {
        CalendarDTO.CalendarDayDTO dto = new CalendarDTO.CalendarDayDTO();

        assertNull(dto.getDate());
        assertNull(dto.getDayOfWeek());
        assertFalse(dto.isBlocked());
        assertNull(dto.getBlockReason());
        assertNull(dto.getTask());
        assertFalse(dto.isWorkDay());
    }

    @Test
    void calendarDayDTO_allArgsConstructor_shouldSetAllFields() {
        LocalDate date = LocalDate.of(2026, 1, 1);
        TaskDTO task = TaskDTO.builder().id(5L).build();

        CalendarDTO.CalendarDayDTO dto = new CalendarDTO.CalendarDayDTO(
                date, "THURSDAY", true, "Ano Novo", task, false
        );

        assertEquals(date, dto.getDate());
        assertEquals("THURSDAY", dto.getDayOfWeek());
        assertTrue(dto.isBlocked());
        assertEquals("Ano Novo", dto.getBlockReason());
        assertSame(task, dto.getTask());
        assertFalse(dto.isWorkDay());
    }

    @Test
    void calendarDayDTO_gettersAndSetters_shouldWork() {
        CalendarDTO.CalendarDayDTO dto = new CalendarDTO.CalendarDayDTO();
        LocalDate date = LocalDate.of(2026, 2, 4);
        TaskDTO task = TaskDTO.builder().id(10L).build();

        dto.setDate(date);
        dto.setDayOfWeek("WEDNESDAY");
        dto.setBlocked(true);
        dto.setBlockReason("Feriado Nacional");
        dto.setTask(task);
        dto.setWorkDay(false);

        assertEquals(date, dto.getDate());
        assertEquals("WEDNESDAY", dto.getDayOfWeek());
        assertTrue(dto.isBlocked());
        assertEquals("Feriado Nacional", dto.getBlockReason());
        assertSame(task, dto.getTask());
        assertFalse(dto.isWorkDay());
    }

    @Test
    void calendarDayDTO_blockedDay_shouldHaveCorrectBooleans() {
        CalendarDTO.CalendarDayDTO dto = CalendarDTO.CalendarDayDTO.builder()
                .date(LocalDate.of(2026, 2, 4))
                .isBlocked(true)
                .blockReason("Dia dos Herois")
                .isWorkDay(false)
                .build();

        assertTrue(dto.isBlocked());
        assertFalse(dto.isWorkDay());
        assertEquals("Dia dos Herois", dto.getBlockReason());
    }

    @Test
    void calendarDayDTO_workDay_shouldHaveCorrectBooleans() {
        CalendarDTO.CalendarDayDTO dto = CalendarDTO.CalendarDayDTO.builder()
                .date(LocalDate.of(2026, 1, 6))
                .isBlocked(false)
                .isWorkDay(true)
                .build();

        assertFalse(dto.isBlocked());
        assertTrue(dto.isWorkDay());
    }

    @Test
    void calendarDayDTO_equals_symmetric() {
        LocalDate date = LocalDate.of(2026, 1, 6);
        CalendarDTO.CalendarDayDTO dto1 = CalendarDTO.CalendarDayDTO.builder()
                .date(date).dayOfWeek("MONDAY").isBlocked(false).isWorkDay(true).build();
        CalendarDTO.CalendarDayDTO dto2 = CalendarDTO.CalendarDayDTO.builder()
                .date(date).dayOfWeek("MONDAY").isBlocked(false).isWorkDay(true).build();

        assertEquals(dto1, dto2);
        assertEquals(dto2, dto1);
    }

    @Test
    void calendarDayDTO_equals_differentValuesReturnsFalse() {
        CalendarDTO.CalendarDayDTO dto1 = CalendarDTO.CalendarDayDTO.builder()
                .date(LocalDate.of(2026, 1, 6)).isBlocked(false).build();
        CalendarDTO.CalendarDayDTO dto2 = CalendarDTO.CalendarDayDTO.builder()
                .date(LocalDate.of(2026, 1, 7)).isBlocked(true).build();

        assertNotEquals(dto1, dto2);
    }

    @Test
    void calendarDayDTO_hashCode_equalObjectsSameHashCode() {
        LocalDate date = LocalDate.of(2026, 1, 6);
        CalendarDTO.CalendarDayDTO dto1 = CalendarDTO.CalendarDayDTO.builder()
                .date(date).dayOfWeek("MONDAY").isWorkDay(true).build();
        CalendarDTO.CalendarDayDTO dto2 = CalendarDTO.CalendarDayDTO.builder()
                .date(date).dayOfWeek("MONDAY").isWorkDay(true).build();

        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void calendarDayDTO_toString_containsFieldValues() {
        CalendarDTO.CalendarDayDTO dto = CalendarDTO.CalendarDayDTO.builder()
                .date(LocalDate.of(2026, 1, 6))
                .dayOfWeek("MONDAY")
                .isWorkDay(true)
                .build();

        String result = dto.toString();
        assertTrue(result.contains("CalendarDayDTO"));
        assertTrue(result.contains("MONDAY"));
        assertTrue(result.contains("2026-01-06"));
    }
}
