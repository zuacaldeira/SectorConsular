package ao.gov.sgcd.pm.service;

import ao.gov.sgcd.pm.dto.BlockedDayDTO;
import ao.gov.sgcd.pm.dto.CalendarDTO;
import ao.gov.sgcd.pm.dto.TaskDTO;
import ao.gov.sgcd.pm.entity.*;
import ao.gov.sgcd.pm.mapper.BlockedDayMapper;
import ao.gov.sgcd.pm.mapper.TaskMapper;
import ao.gov.sgcd.pm.repository.BlockedDayRepository;
import ao.gov.sgcd.pm.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CalendarServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private BlockedDayRepository blockedDayRepository;

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private BlockedDayMapper blockedDayMapper;

    @InjectMocks
    private CalendarService calendarService;

    // --- Helper builders ---

    private Sprint buildSprint(Long id, int number) {
        return Sprint.builder()
                .id(id)
                .sprintNumber(number)
                .name("Sprint " + number)
                .nameEn("Sprint " + number + " EN")
                .status(SprintStatus.ACTIVE)
                .totalSessions(36)
                .completedSessions(10)
                .totalHours(108)
                .actualHours(BigDecimal.valueOf(30))
                .weeks(4)
                .startDate(LocalDate.of(2026, 3, 2))
                .endDate(LocalDate.of(2026, 4, 12))
                .build();
    }

    private Task buildTask(Long id, String taskCode, LocalDate sessionDate) {
        Sprint sprint = buildSprint(1L, 1);
        return Task.builder()
                .id(id)
                .taskCode(taskCode)
                .title("Tarefa " + taskCode)
                .sprint(sprint)
                .status(TaskStatus.PLANNED)
                .sessionDate(sessionDate)
                .dayOfWeek(sessionDate.getDayOfWeek().name().substring(0, 3))
                .weekNumber(1)
                .plannedHours(BigDecimal.valueOf(3.5))
                .sortOrder(1)
                .notes(new ArrayList<>())
                .executions(new ArrayList<>())
                .build();
    }

    private BlockedDay buildBlockedDay(Long id, LocalDate date, String reason) {
        return BlockedDay.builder()
                .id(id)
                .blockedDate(date)
                .dayOfWeek(date.getDayOfWeek().name().substring(0, 3))
                .blockType(BlockType.HOLIDAY)
                .reason(reason)
                .hoursLost(BigDecimal.valueOf(3.5))
                .build();
    }

    // =====================================================================
    // getCalendar
    // =====================================================================

    @Test
    void getCalendar_shouldReturnCorrectDaysForMarch2026() {
        // given - March 2026 has 31 days
        LocalDate from = LocalDate.of(2026, 3, 1);
        LocalDate to = LocalDate.of(2026, 3, 31);

        when(taskRepository.findByDateRange(from, to)).thenReturn(Collections.emptyList());
        when(blockedDayRepository.findByDateRange(from, to)).thenReturn(Collections.emptyList());

        // when
        CalendarDTO result = calendarService.getCalendar(2026, 3);

        // then
        assertNotNull(result);
        assertEquals(2026, result.getYear());
        assertEquals(3, result.getMonth());
        assertNotNull(result.getDays());
        assertEquals(31, result.getDays().size());
    }

    @Test
    void getCalendar_shouldReturnCorrectDaysForFebruary2026() {
        // given - February 2026 has 28 days (not a leap year)
        LocalDate from = LocalDate.of(2026, 2, 1);
        LocalDate to = LocalDate.of(2026, 2, 28);

        when(taskRepository.findByDateRange(from, to)).thenReturn(Collections.emptyList());
        when(blockedDayRepository.findByDateRange(from, to)).thenReturn(Collections.emptyList());

        // when
        CalendarDTO result = calendarService.getCalendar(2026, 2);

        // then
        assertEquals(28, result.getDays().size());
    }

    @Test
    void getCalendar_shouldMapTasksToCorrectDays() {
        // given
        LocalDate from = LocalDate.of(2026, 3, 1);
        LocalDate to = LocalDate.of(2026, 3, 31);

        LocalDate taskDate = LocalDate.of(2026, 3, 10); // Tuesday
        Task task = buildTask(1L, "S1-06", taskDate);
        when(taskRepository.findByDateRange(from, to)).thenReturn(List.of(task));
        when(blockedDayRepository.findByDateRange(from, to)).thenReturn(Collections.emptyList());

        TaskDTO taskDto = TaskDTO.builder()
                .id(1L).taskCode("S1-06").title("Tarefa S1-06")
                .status(TaskStatus.PLANNED).build();
        when(taskMapper.toDto(task)).thenReturn(taskDto);

        // when
        CalendarDTO result = calendarService.getCalendar(2026, 3);

        // then
        // Day 10 (index 9) should have the task
        CalendarDTO.CalendarDayDTO day10 = result.getDays().get(9);
        assertEquals(taskDate, day10.getDate());
        assertNotNull(day10.getTask());
        assertEquals("S1-06", day10.getTask().getTaskCode());

        // Day 1 (index 0) should NOT have a task
        CalendarDTO.CalendarDayDTO day1 = result.getDays().get(0);
        assertNull(day1.getTask());
    }

    @Test
    void getCalendar_shouldMapBlockedDaysCorrectly() {
        // given
        LocalDate from = LocalDate.of(2026, 3, 1);
        LocalDate to = LocalDate.of(2026, 3, 31);

        LocalDate blockedDate = LocalDate.of(2026, 3, 19); // Thursday (Feriado)
        BlockedDay blockedDay = buildBlockedDay(1L, blockedDate, "Dia de Sao Jose");

        when(taskRepository.findByDateRange(from, to)).thenReturn(Collections.emptyList());
        when(blockedDayRepository.findByDateRange(from, to)).thenReturn(List.of(blockedDay));

        // when
        CalendarDTO result = calendarService.getCalendar(2026, 3);

        // then
        // Day 19 (index 18) should be blocked
        CalendarDTO.CalendarDayDTO day19 = result.getDays().get(18);
        assertEquals(blockedDate, day19.getDate());
        assertTrue(day19.isBlocked());
        assertEquals("Dia de Sao Jose", day19.getBlockReason());

        // Day 1 (index 0) should NOT be blocked
        CalendarDTO.CalendarDayDTO day1 = result.getDays().get(0);
        assertFalse(day1.isBlocked());
        assertNull(day1.getBlockReason());
    }

    @Test
    void getCalendar_shouldMarkSaturdaysAsNonWorkDays() {
        // given - March 2026: first Saturday is March 7
        LocalDate from = LocalDate.of(2026, 3, 1);
        LocalDate to = LocalDate.of(2026, 3, 31);

        when(taskRepository.findByDateRange(from, to)).thenReturn(Collections.emptyList());
        when(blockedDayRepository.findByDateRange(from, to)).thenReturn(Collections.emptyList());

        // when
        CalendarDTO result = calendarService.getCalendar(2026, 3);

        // then
        // March 7, 2026 is a Saturday (index 6)
        CalendarDTO.CalendarDayDTO saturday = result.getDays().get(6);
        assertEquals(LocalDate.of(2026, 3, 7), saturday.getDate());
        assertEquals("SAT", saturday.getDayOfWeek());
        assertFalse(saturday.isWorkDay());

        // March 8, 2026 is a Sunday (should be work day per logic: dow != SATURDAY)
        CalendarDTO.CalendarDayDTO sunday = result.getDays().get(7);
        assertEquals(LocalDate.of(2026, 3, 8), sunday.getDate());
        assertEquals("SUN", sunday.getDayOfWeek());
        assertTrue(sunday.isWorkDay());

        // March 9, 2026 is a Monday (should be work day)
        CalendarDTO.CalendarDayDTO monday = result.getDays().get(8);
        assertEquals(LocalDate.of(2026, 3, 9), monday.getDate());
        assertEquals("MON", monday.getDayOfWeek());
        assertTrue(monday.isWorkDay());
    }

    @Test
    void getCalendar_shouldSetDayOfWeekAbbreviation() {
        // given
        LocalDate from = LocalDate.of(2026, 3, 1);
        LocalDate to = LocalDate.of(2026, 3, 31);

        when(taskRepository.findByDateRange(from, to)).thenReturn(Collections.emptyList());
        when(blockedDayRepository.findByDateRange(from, to)).thenReturn(Collections.emptyList());

        // when
        CalendarDTO result = calendarService.getCalendar(2026, 3);

        // then
        // March 1, 2026 is a Sunday
        CalendarDTO.CalendarDayDTO day1 = result.getDays().get(0);
        assertEquals("SUN", day1.getDayOfWeek());

        // March 2, 2026 is a Monday
        CalendarDTO.CalendarDayDTO day2 = result.getDays().get(1);
        assertEquals("MON", day2.getDayOfWeek());
    }

    @Test
    void getCalendar_shouldHandleMonthWithBothTasksAndBlockedDays() {
        // given
        LocalDate from = LocalDate.of(2026, 4, 1);
        LocalDate to = LocalDate.of(2026, 4, 30);

        LocalDate taskDate = LocalDate.of(2026, 4, 6); // Monday
        Task task = buildTask(1L, "S1-30", taskDate);
        TaskDTO taskDto = TaskDTO.builder().id(1L).taskCode("S1-30").build();
        when(taskMapper.toDto(task)).thenReturn(taskDto);

        LocalDate blockedDate = LocalDate.of(2026, 4, 10); // Friday
        BlockedDay blocked = buildBlockedDay(1L, blockedDate, "Sexta-feira Santa");

        when(taskRepository.findByDateRange(from, to)).thenReturn(List.of(task));
        when(blockedDayRepository.findByDateRange(from, to)).thenReturn(List.of(blocked));

        // when
        CalendarDTO result = calendarService.getCalendar(2026, 4);

        // then
        assertEquals(30, result.getDays().size());

        // Day 6 (index 5) has task
        CalendarDTO.CalendarDayDTO day6 = result.getDays().get(5);
        assertNotNull(day6.getTask());
        assertFalse(day6.isBlocked());

        // Day 10 (index 9) is blocked
        CalendarDTO.CalendarDayDTO day10 = result.getDays().get(9);
        assertTrue(day10.isBlocked());
        assertEquals("Sexta-feira Santa", day10.getBlockReason());
    }

    // =====================================================================
    // getBlockedDays
    // =====================================================================

    @Test
    void getBlockedDays_shouldReturnAllBlockedDays() {
        // given
        BlockedDay blocked1 = buildBlockedDay(1L, LocalDate.of(2026, 4, 10), "Sexta-feira Santa");
        BlockedDay blocked2 = buildBlockedDay(2L, LocalDate.of(2026, 6, 1), "Dia da Crianca");

        BlockedDayDTO dto1 = BlockedDayDTO.builder()
                .id(1L).blockedDate(LocalDate.of(2026, 4, 10))
                .reason("Sexta-feira Santa").blockType(BlockType.HOLIDAY).build();
        BlockedDayDTO dto2 = BlockedDayDTO.builder()
                .id(2L).blockedDate(LocalDate.of(2026, 6, 1))
                .reason("Dia da Crianca").blockType(BlockType.HOLIDAY).build();

        when(blockedDayRepository.findAll()).thenReturn(List.of(blocked1, blocked2));
        when(blockedDayMapper.toDtoList(List.of(blocked1, blocked2))).thenReturn(List.of(dto1, dto2));

        // when
        List<BlockedDayDTO> result = calendarService.getBlockedDays();

        // then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Sexta-feira Santa", result.get(0).getReason());
        assertEquals("Dia da Crianca", result.get(1).getReason());
        verify(blockedDayRepository).findAll();
    }

    @Test
    void getBlockedDays_shouldReturnEmptyListWhenNone() {
        // given
        when(blockedDayRepository.findAll()).thenReturn(Collections.emptyList());
        when(blockedDayMapper.toDtoList(Collections.emptyList())).thenReturn(Collections.emptyList());

        // when
        List<BlockedDayDTO> result = calendarService.getBlockedDays();

        // then
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
