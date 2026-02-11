package ao.gov.sgcd.pm.dto;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DashboardDTOTest {

    // --- DashboardDTO tests ---

    @Test
    void builder_shouldCreateDTOWithAllFields() {
        SprintDTO activeSprint = SprintDTO.builder().id(1L).name("Sprint 1").build();
        TaskDTO todayTask = TaskDTO.builder().id(1L).taskCode("S1-D01").build();
        List<TaskDTO> recentTasks = new ArrayList<>();
        List<DashboardDTO.SprintSummaryDTO> sprintSummaries = new ArrayList<>();
        List<BlockedDayDTO> upcomingBlockedDays = new ArrayList<>();
        DashboardDTO.WeekProgressDTO weekProgress = DashboardDTO.WeekProgressDTO.builder()
                .weekTasks(5)
                .weekCompleted(3)
                .build();

        DashboardDTO dto = DashboardDTO.builder()
                .projectProgress(45.5)
                .totalSessions(204)
                .completedSessions(92)
                .totalHoursPlanned(680)
                .totalHoursSpent(BigDecimal.valueOf(310.5))
                .activeSprint(activeSprint)
                .todayTask(todayTask)
                .recentTasks(recentTasks)
                .sprintSummaries(sprintSummaries)
                .upcomingBlockedDays(upcomingBlockedDays)
                .weekProgress(weekProgress)
                .build();

        assertEquals(45.5, dto.getProjectProgress());
        assertEquals(204, dto.getTotalSessions());
        assertEquals(92, dto.getCompletedSessions());
        assertEquals(680, dto.getTotalHoursPlanned());
        assertEquals(BigDecimal.valueOf(310.5), dto.getTotalHoursSpent());
        assertSame(activeSprint, dto.getActiveSprint());
        assertSame(todayTask, dto.getTodayTask());
        assertSame(recentTasks, dto.getRecentTasks());
        assertSame(sprintSummaries, dto.getSprintSummaries());
        assertSame(upcomingBlockedDays, dto.getUpcomingBlockedDays());
        assertSame(weekProgress, dto.getWeekProgress());
    }

    @Test
    void noArgConstructor_shouldCreateEmptyDTO() {
        DashboardDTO dto = new DashboardDTO();

        assertNull(dto.getProjectProgress());
        assertNull(dto.getTotalSessions());
        assertNull(dto.getCompletedSessions());
        assertNull(dto.getTotalHoursPlanned());
        assertNull(dto.getTotalHoursSpent());
        assertNull(dto.getActiveSprint());
        assertNull(dto.getTodayTask());
        assertNull(dto.getRecentTasks());
        assertNull(dto.getSprintSummaries());
        assertNull(dto.getUpcomingBlockedDays());
        assertNull(dto.getWeekProgress());
    }

    @Test
    void allArgsConstructor_shouldSetAllFields() {
        SprintDTO activeSprint = SprintDTO.builder().id(2L).build();
        TaskDTO todayTask = TaskDTO.builder().id(5L).build();
        List<TaskDTO> recentTasks = List.of(todayTask);
        List<DashboardDTO.SprintSummaryDTO> sprintSummaries = new ArrayList<>();
        List<BlockedDayDTO> upcomingBlockedDays = new ArrayList<>();
        DashboardDTO.WeekProgressDTO weekProgress = DashboardDTO.WeekProgressDTO.builder().weekTasks(3).build();

        DashboardDTO dto = new DashboardDTO(
                50.0, 204, 102, 680, BigDecimal.valueOf(340.0),
                activeSprint, todayTask, recentTasks, sprintSummaries,
                upcomingBlockedDays, weekProgress
        );

        assertEquals(50.0, dto.getProjectProgress());
        assertEquals(204, dto.getTotalSessions());
        assertEquals(102, dto.getCompletedSessions());
        assertEquals(680, dto.getTotalHoursPlanned());
        assertEquals(BigDecimal.valueOf(340.0), dto.getTotalHoursSpent());
        assertSame(activeSprint, dto.getActiveSprint());
        assertSame(todayTask, dto.getTodayTask());
        assertSame(recentTasks, dto.getRecentTasks());
        assertSame(sprintSummaries, dto.getSprintSummaries());
        assertSame(upcomingBlockedDays, dto.getUpcomingBlockedDays());
        assertSame(weekProgress, dto.getWeekProgress());
    }

    @Test
    void gettersAndSetters_shouldWorkForAllFields() {
        DashboardDTO dto = new DashboardDTO();
        SprintDTO activeSprint = SprintDTO.builder().id(1L).build();
        TaskDTO todayTask = TaskDTO.builder().id(1L).build();
        List<TaskDTO> recentTasks = new ArrayList<>();
        List<DashboardDTO.SprintSummaryDTO> sprintSummaries = new ArrayList<>();
        List<BlockedDayDTO> upcomingBlockedDays = new ArrayList<>();
        DashboardDTO.WeekProgressDTO weekProgress = DashboardDTO.WeekProgressDTO.builder().build();

        dto.setProjectProgress(60.0);
        dto.setTotalSessions(204);
        dto.setCompletedSessions(122);
        dto.setTotalHoursPlanned(680);
        dto.setTotalHoursSpent(BigDecimal.valueOf(400.0));
        dto.setActiveSprint(activeSprint);
        dto.setTodayTask(todayTask);
        dto.setRecentTasks(recentTasks);
        dto.setSprintSummaries(sprintSummaries);
        dto.setUpcomingBlockedDays(upcomingBlockedDays);
        dto.setWeekProgress(weekProgress);

        assertEquals(60.0, dto.getProjectProgress());
        assertEquals(204, dto.getTotalSessions());
        assertEquals(122, dto.getCompletedSessions());
        assertEquals(680, dto.getTotalHoursPlanned());
        assertEquals(BigDecimal.valueOf(400.0), dto.getTotalHoursSpent());
        assertSame(activeSprint, dto.getActiveSprint());
        assertSame(todayTask, dto.getTodayTask());
        assertSame(recentTasks, dto.getRecentTasks());
        assertSame(sprintSummaries, dto.getSprintSummaries());
        assertSame(upcomingBlockedDays, dto.getUpcomingBlockedDays());
        assertSame(weekProgress, dto.getWeekProgress());
    }

    @Test
    void equals_reflexive() {
        DashboardDTO dto = DashboardDTO.builder().projectProgress(50.0).totalSessions(204).build();
        assertEquals(dto, dto);
    }

    @Test
    void equals_symmetric() {
        DashboardDTO dto1 = DashboardDTO.builder().projectProgress(50.0).totalSessions(204).build();
        DashboardDTO dto2 = DashboardDTO.builder().projectProgress(50.0).totalSessions(204).build();

        assertEquals(dto1, dto2);
        assertEquals(dto2, dto1);
    }

    @Test
    void equals_nullReturnsFalse() {
        DashboardDTO dto = DashboardDTO.builder().projectProgress(50.0).build();
        assertNotEquals(null, dto);
    }

    @Test
    void equals_differentClassReturnsFalse() {
        DashboardDTO dto = DashboardDTO.builder().projectProgress(50.0).build();
        assertNotEquals("a string", dto);
    }

    @Test
    void equals_differentValuesReturnsFalse() {
        DashboardDTO dto1 = DashboardDTO.builder().projectProgress(50.0).totalSessions(204).build();
        DashboardDTO dto2 = DashboardDTO.builder().projectProgress(75.0).totalSessions(204).build();

        assertNotEquals(dto1, dto2);
    }

    @Test
    void hashCode_equalObjectsSameHashCode() {
        DashboardDTO dto1 = DashboardDTO.builder().projectProgress(50.0).totalSessions(204).build();
        DashboardDTO dto2 = DashboardDTO.builder().projectProgress(50.0).totalSessions(204).build();

        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void hashCode_differentObjectsDifferentHashCode() {
        DashboardDTO dto1 = DashboardDTO.builder().projectProgress(50.0).build();
        DashboardDTO dto2 = DashboardDTO.builder().projectProgress(75.0).build();

        assertNotEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void toString_containsClassNameAndFieldValues() {
        DashboardDTO dto = DashboardDTO.builder()
                .projectProgress(45.5)
                .totalSessions(204)
                .build();

        String result = dto.toString();
        assertTrue(result.contains("DashboardDTO"));
        assertTrue(result.contains("45.5"));
        assertTrue(result.contains("204"));
    }

    // --- SprintSummaryDTO tests ---

    @Test
    void sprintSummaryDTO_builder_shouldCreateWithAllFields() {
        DashboardDTO.SprintSummaryDTO dto = DashboardDTO.SprintSummaryDTO.builder()
                .sprintNumber(1)
                .name("Sprint 1 - Fundacao")
                .progress(75.0)
                .status("ACTIVE")
                .color("#CC092F")
                .build();

        assertEquals(1, dto.getSprintNumber());
        assertEquals("Sprint 1 - Fundacao", dto.getName());
        assertEquals(75.0, dto.getProgress());
        assertEquals("ACTIVE", dto.getStatus());
        assertEquals("#CC092F", dto.getColor());
    }

    @Test
    void sprintSummaryDTO_noArgConstructor_shouldCreateEmpty() {
        DashboardDTO.SprintSummaryDTO dto = new DashboardDTO.SprintSummaryDTO();

        assertNull(dto.getSprintNumber());
        assertNull(dto.getName());
        assertNull(dto.getProgress());
        assertNull(dto.getStatus());
        assertNull(dto.getColor());
    }

    @Test
    void sprintSummaryDTO_allArgsConstructor_shouldSetAllFields() {
        DashboardDTO.SprintSummaryDTO dto = new DashboardDTO.SprintSummaryDTO(
                2, "Sprint 2", 50.0, "PLANNED", "#1A1A1A"
        );

        assertEquals(2, dto.getSprintNumber());
        assertEquals("Sprint 2", dto.getName());
        assertEquals(50.0, dto.getProgress());
        assertEquals("PLANNED", dto.getStatus());
        assertEquals("#1A1A1A", dto.getColor());
    }

    @Test
    void sprintSummaryDTO_gettersAndSetters_shouldWork() {
        DashboardDTO.SprintSummaryDTO dto = new DashboardDTO.SprintSummaryDTO();

        dto.setSprintNumber(3);
        dto.setName("Sprint 3");
        dto.setProgress(100.0);
        dto.setStatus("COMPLETED");
        dto.setColor("#F4B400");

        assertEquals(3, dto.getSprintNumber());
        assertEquals("Sprint 3", dto.getName());
        assertEquals(100.0, dto.getProgress());
        assertEquals("COMPLETED", dto.getStatus());
        assertEquals("#F4B400", dto.getColor());
    }

    @Test
    void sprintSummaryDTO_equals_symmetric() {
        DashboardDTO.SprintSummaryDTO dto1 = DashboardDTO.SprintSummaryDTO.builder()
                .sprintNumber(1).name("Sprint 1").progress(50.0).build();
        DashboardDTO.SprintSummaryDTO dto2 = DashboardDTO.SprintSummaryDTO.builder()
                .sprintNumber(1).name("Sprint 1").progress(50.0).build();

        assertEquals(dto1, dto2);
        assertEquals(dto2, dto1);
    }

    @Test
    void sprintSummaryDTO_equals_differentValuesReturnsFalse() {
        DashboardDTO.SprintSummaryDTO dto1 = DashboardDTO.SprintSummaryDTO.builder()
                .sprintNumber(1).name("Sprint 1").build();
        DashboardDTO.SprintSummaryDTO dto2 = DashboardDTO.SprintSummaryDTO.builder()
                .sprintNumber(2).name("Sprint 2").build();

        assertNotEquals(dto1, dto2);
    }

    @Test
    void sprintSummaryDTO_hashCode_equalObjectsSameHashCode() {
        DashboardDTO.SprintSummaryDTO dto1 = DashboardDTO.SprintSummaryDTO.builder()
                .sprintNumber(1).name("Sprint 1").build();
        DashboardDTO.SprintSummaryDTO dto2 = DashboardDTO.SprintSummaryDTO.builder()
                .sprintNumber(1).name("Sprint 1").build();

        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void sprintSummaryDTO_toString_containsFieldValues() {
        DashboardDTO.SprintSummaryDTO dto = DashboardDTO.SprintSummaryDTO.builder()
                .sprintNumber(1)
                .name("Sprint 1")
                .build();

        String result = dto.toString();
        assertTrue(result.contains("SprintSummaryDTO"));
        assertTrue(result.contains("Sprint 1"));
    }

    // --- WeekProgressDTO tests ---

    @Test
    void weekProgressDTO_builder_shouldCreateWithAllFields() {
        DashboardDTO.WeekProgressDTO dto = DashboardDTO.WeekProgressDTO.builder()
                .weekTasks(5)
                .weekCompleted(3)
                .weekHoursPlanned(BigDecimal.valueOf(17.5))
                .weekHoursSpent(BigDecimal.valueOf(10.0))
                .build();

        assertEquals(5, dto.getWeekTasks());
        assertEquals(3, dto.getWeekCompleted());
        assertEquals(BigDecimal.valueOf(17.5), dto.getWeekHoursPlanned());
        assertEquals(BigDecimal.valueOf(10.0), dto.getWeekHoursSpent());
    }

    @Test
    void weekProgressDTO_noArgConstructor_shouldCreateEmpty() {
        DashboardDTO.WeekProgressDTO dto = new DashboardDTO.WeekProgressDTO();

        assertNull(dto.getWeekTasks());
        assertNull(dto.getWeekCompleted());
        assertNull(dto.getWeekHoursPlanned());
        assertNull(dto.getWeekHoursSpent());
    }

    @Test
    void weekProgressDTO_allArgsConstructor_shouldSetAllFields() {
        DashboardDTO.WeekProgressDTO dto = new DashboardDTO.WeekProgressDTO(
                4, 2, BigDecimal.valueOf(14.0), BigDecimal.valueOf(7.0)
        );

        assertEquals(4, dto.getWeekTasks());
        assertEquals(2, dto.getWeekCompleted());
        assertEquals(BigDecimal.valueOf(14.0), dto.getWeekHoursPlanned());
        assertEquals(BigDecimal.valueOf(7.0), dto.getWeekHoursSpent());
    }

    @Test
    void weekProgressDTO_gettersAndSetters_shouldWork() {
        DashboardDTO.WeekProgressDTO dto = new DashboardDTO.WeekProgressDTO();

        dto.setWeekTasks(6);
        dto.setWeekCompleted(4);
        dto.setWeekHoursPlanned(BigDecimal.valueOf(21.0));
        dto.setWeekHoursSpent(BigDecimal.valueOf(14.0));

        assertEquals(6, dto.getWeekTasks());
        assertEquals(4, dto.getWeekCompleted());
        assertEquals(BigDecimal.valueOf(21.0), dto.getWeekHoursPlanned());
        assertEquals(BigDecimal.valueOf(14.0), dto.getWeekHoursSpent());
    }

    @Test
    void weekProgressDTO_equals_symmetric() {
        DashboardDTO.WeekProgressDTO dto1 = DashboardDTO.WeekProgressDTO.builder()
                .weekTasks(5).weekCompleted(3).build();
        DashboardDTO.WeekProgressDTO dto2 = DashboardDTO.WeekProgressDTO.builder()
                .weekTasks(5).weekCompleted(3).build();

        assertEquals(dto1, dto2);
        assertEquals(dto2, dto1);
    }

    @Test
    void weekProgressDTO_equals_differentValuesReturnsFalse() {
        DashboardDTO.WeekProgressDTO dto1 = DashboardDTO.WeekProgressDTO.builder().weekTasks(5).build();
        DashboardDTO.WeekProgressDTO dto2 = DashboardDTO.WeekProgressDTO.builder().weekTasks(3).build();

        assertNotEquals(dto1, dto2);
    }

    @Test
    void weekProgressDTO_hashCode_equalObjectsSameHashCode() {
        DashboardDTO.WeekProgressDTO dto1 = DashboardDTO.WeekProgressDTO.builder()
                .weekTasks(5).weekCompleted(3).build();
        DashboardDTO.WeekProgressDTO dto2 = DashboardDTO.WeekProgressDTO.builder()
                .weekTasks(5).weekCompleted(3).build();

        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void weekProgressDTO_toString_containsFieldValues() {
        DashboardDTO.WeekProgressDTO dto = DashboardDTO.WeekProgressDTO.builder()
                .weekTasks(5)
                .weekCompleted(3)
                .build();

        String result = dto.toString();
        assertTrue(result.contains("WeekProgressDTO"));
        assertTrue(result.contains("5"));
        assertTrue(result.contains("3"));
    }
}
