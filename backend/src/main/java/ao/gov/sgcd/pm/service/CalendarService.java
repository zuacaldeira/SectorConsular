package ao.gov.sgcd.pm.service;

import ao.gov.sgcd.pm.dto.BlockedDayDTO;
import ao.gov.sgcd.pm.dto.CalendarDTO;
import ao.gov.sgcd.pm.entity.BlockedDay;
import ao.gov.sgcd.pm.entity.Task;
import ao.gov.sgcd.pm.mapper.BlockedDayMapper;
import ao.gov.sgcd.pm.mapper.TaskMapper;
import ao.gov.sgcd.pm.repository.BlockedDayRepository;
import ao.gov.sgcd.pm.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CalendarService {

    private final TaskRepository taskRepository;
    private final BlockedDayRepository blockedDayRepository;
    private final TaskMapper taskMapper;
    private final BlockedDayMapper blockedDayMapper;

    public CalendarDTO getCalendar(int year, int month) {
        YearMonth ym = YearMonth.of(year, month);
        LocalDate from = ym.atDay(1);
        LocalDate to = ym.atEndOfMonth();

        List<Task> tasks = taskRepository.findByDateRange(from, to);
        Map<LocalDate, Task> taskMap = tasks.stream()
                .collect(Collectors.toMap(Task::getSessionDate, Function.identity(), (a, b) -> a));

        List<BlockedDay> blockedDays = blockedDayRepository.findByDateRange(from, to);
        Map<LocalDate, BlockedDay> blockedMap = blockedDays.stream()
                .collect(Collectors.toMap(BlockedDay::getBlockedDate, Function.identity()));

        List<CalendarDTO.CalendarDayDTO> days = new ArrayList<>();
        for (LocalDate date = from; !date.isAfter(to); date = date.plusDays(1)) {
            DayOfWeek dow = date.getDayOfWeek();
            boolean isWorkDay = dow != DayOfWeek.SATURDAY
                    && (dow == DayOfWeek.SUNDAY || (dow != DayOfWeek.SATURDAY));
            // Work days: Mon-Fri + Sunday
            isWorkDay = dow != DayOfWeek.SATURDAY;

            BlockedDay blocked = blockedMap.get(date);
            Task task = taskMap.get(date);

            days.add(CalendarDTO.CalendarDayDTO.builder()
                    .date(date)
                    .dayOfWeek(dow.name().substring(0, 3))
                    .isBlocked(blocked != null)
                    .blockReason(blocked != null ? blocked.getReason() : null)
                    .task(task != null ? taskMapper.toDto(task) : null)
                    .isWorkDay(isWorkDay)
                    .build());
        }

        return CalendarDTO.builder()
                .year(year)
                .month(month)
                .days(days)
                .build();
    }

    public List<BlockedDayDTO> getBlockedDays() {
        return blockedDayMapper.toDtoList(blockedDayRepository.findAll());
    }
}
