package ao.gov.sgcd.pm.service;

import ao.gov.sgcd.pm.dto.*;
import ao.gov.sgcd.pm.entity.*;
import ao.gov.sgcd.pm.mapper.*;
import ao.gov.sgcd.pm.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final SprintRepository sprintRepository;
    private final TaskRepository taskRepository;
    private final BlockedDayRepository blockedDayRepository;
    private final ProjectConfigRepository configRepository;
    private final SprintMapper sprintMapper;
    private final TaskMapper taskMapper;
    private final BlockedDayMapper blockedDayMapper;

    public DashboardDTO getDeveloperDashboard() {
        int totalSessions = 204;
        int completedSessions = taskRepository.countByStatus(TaskStatus.COMPLETED);
        BigDecimal totalHoursSpent = taskRepository.sumActualHoursCompleted();
        double projectProgress = totalSessions > 0 ? (completedSessions * 100.0) / totalSessions : 0;

        // Active sprint
        SprintDTO activeSprint = sprintRepository.findActiveSprint()
                .map(s -> {
                    SprintDTO dto = sprintMapper.toDto(s);
                    dto.setProgressPercent(s.getTotalSessions() > 0
                            ? (s.getCompletedSessions() * 100.0) / s.getTotalSessions() : 0);
                    return dto;
                })
                .orElse(null);

        // Today's task
        TaskDTO todayTask = null;
        var todayOpt = taskRepository.findBySessionDate(LocalDate.now());
        if (todayOpt.isPresent()) {
            todayTask = taskMapper.toDto(todayOpt.get());
        } else {
            var upcoming = taskRepository.findUpcomingPlanned(LocalDate.now());
            if (!upcoming.isEmpty()) {
                todayTask = taskMapper.toDto(upcoming.get(0));
            }
        }

        // Recent tasks
        List<TaskDTO> recentTasks = taskRepository.findRecentCompleted(
                        org.springframework.data.domain.PageRequest.of(0, 5))
                .stream().map(taskMapper::toDto).toList();

        // Sprint summaries
        List<DashboardDTO.SprintSummaryDTO> sprintSummaries = sprintRepository.findAllOrdered().stream()
                .map(s -> DashboardDTO.SprintSummaryDTO.builder()
                        .sprintNumber(s.getSprintNumber())
                        .name(s.getName())
                        .progress(s.getTotalSessions() > 0
                                ? (s.getCompletedSessions() * 100.0) / s.getTotalSessions() : 0)
                        .status(s.getStatus().name())
                        .color(s.getColor())
                        .build())
                .toList();

        // Upcoming blocked days
        List<BlockedDayDTO> upcomingBlocked = blockedDayMapper.toDtoList(
                blockedDayRepository.findUpcoming(LocalDate.now()));

        // Week progress
        LocalDate weekStart = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate weekEnd = weekStart.plusDays(6);
        List<Task> weekTasks = taskRepository.findByWeek(weekStart, weekEnd);
        int weekCompleted = (int) weekTasks.stream().filter(t -> t.getStatus() == TaskStatus.COMPLETED).count();
        BigDecimal weekHoursPlanned = weekTasks.stream()
                .map(Task::getPlannedHours)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal weekHoursSpent = weekTasks.stream()
                .filter(t -> t.getActualHours() != null)
                .map(Task::getActualHours)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return DashboardDTO.builder()
                .projectProgress(projectProgress)
                .totalSessions(totalSessions)
                .completedSessions(completedSessions)
                .totalHoursPlanned(680)
                .totalHoursSpent(totalHoursSpent)
                .activeSprint(activeSprint)
                .todayTask(todayTask)
                .recentTasks(recentTasks)
                .sprintSummaries(sprintSummaries)
                .upcomingBlockedDays(upcomingBlocked)
                .weekProgress(DashboardDTO.WeekProgressDTO.builder()
                        .weekTasks(weekTasks.size())
                        .weekCompleted(weekCompleted)
                        .weekHoursPlanned(weekHoursPlanned)
                        .weekHoursSpent(weekHoursSpent)
                        .build())
                .build();
    }

    public StakeholderDashboardDTO getStakeholderDashboard() {
        int totalSessions = 204;
        int completedSessions = taskRepository.countByStatus(TaskStatus.COMPLETED);
        BigDecimal totalHoursSpent = taskRepository.sumActualHoursCompleted();
        double overallProgress = totalSessions > 0 ? (completedSessions * 100.0) / totalSessions : 0;

        LocalDate startDate = LocalDate.parse("2026-03-02");
        LocalDate targetDate = LocalDate.parse("2026-12-20");
        long daysRemaining = ChronoUnit.DAYS.between(LocalDate.now(), targetDate);

        // Sprint details
        List<StakeholderDashboardDTO.StakeholderSprintDTO> sprints = sprintRepository.findAllOrdered().stream()
                .map(s -> StakeholderDashboardDTO.StakeholderSprintDTO.builder()
                        .number(s.getSprintNumber())
                        .name(s.getName())
                        .nameEn(s.getNameEn())
                        .progress(s.getTotalSessions() > 0
                                ? (s.getCompletedSessions() * 100.0) / s.getTotalSessions() : 0)
                        .status(s.getStatus().name())
                        .startDate(s.getStartDate())
                        .endDate(s.getEndDate())
                        .sessions(s.getTotalSessions())
                        .completedSessions(s.getCompletedSessions())
                        .hours(s.getTotalHours())
                        .hoursSpent(s.getActualHours())
                        .color(s.getColor())
                        .focus(s.getFocus())
                        .build())
                .toList();

        // Milestones
        List<StakeholderDashboardDTO.MilestoneDTO> milestones = sprintRepository.findAllOrdered().stream()
                .map(s -> {
                    String milestoneStatus = switch (s.getStatus()) {
                        case COMPLETED -> "COMPLETED";
                        case ACTIVE -> "IN_PROGRESS";
                        case PLANNED -> "FUTURE";
                    };
                    return StakeholderDashboardDTO.MilestoneDTO.builder()
                            .name("Sprint " + s.getSprintNumber() + " Complete")
                            .targetDate(s.getEndDate())
                            .status(milestoneStatus)
                            .build();
                })
                .collect(Collectors.toList());
        milestones.add(StakeholderDashboardDTO.MilestoneDTO.builder()
                .name("Go-Live")
                .targetDate(targetDate)
                .status(daysRemaining <= 0 ? "COMPLETED" : "FUTURE")
                .build());

        // Weekly activity
        LocalDate weekStart = LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate weekEnd = weekStart.plusDays(6);
        List<Task> weekTasks = taskRepository.findByWeek(weekStart, weekEnd);
        int weekCompleted = (int) weekTasks.stream().filter(t -> t.getStatus() == TaskStatus.COMPLETED).count();
        BigDecimal weekHours = weekTasks.stream()
                .filter(t -> t.getActualHours() != null)
                .map(Task::getActualHours)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return StakeholderDashboardDTO.builder()
                .projectName("SGCD — Sistema de Gestão Consular Digital")
                .client("Embaixada da República de Angola")
                .overallProgress(overallProgress)
                .totalSessions(totalSessions)
                .completedSessions(completedSessions)
                .totalHoursPlanned(680)
                .totalHoursSpent(totalHoursSpent)
                .startDate(startDate)
                .targetDate(targetDate)
                .daysRemaining(Math.max(0, daysRemaining))
                .sprints(sprints)
                .milestones(milestones)
                .weeklyActivity(StakeholderDashboardDTO.WeeklyActivityDTO.builder()
                        .sessionsThisWeek(weekTasks.size())
                        .hoursThisWeek(weekHours)
                        .tasksCompletedThisWeek(weekCompleted)
                        .build())
                .lastUpdated(LocalDateTime.now())
                .build();
    }
}
