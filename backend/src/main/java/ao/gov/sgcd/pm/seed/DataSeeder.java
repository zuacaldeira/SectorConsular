package ao.gov.sgcd.pm.seed;

import ao.gov.sgcd.pm.entity.Sprint;
import ao.gov.sgcd.pm.entity.Task;
import ao.gov.sgcd.pm.entity.TaskStatus;
import ao.gov.sgcd.pm.repository.SprintRepository;
import ao.gov.sgcd.pm.repository.TaskRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

    private final TaskRepository taskRepository;
    private final SprintRepository sprintRepository;
    private final ObjectMapper objectMapper;

    // Sprint start dates for calculating week_number
    private static final Map<Integer, LocalDate> SPRINT_START_DATES = Map.of(
            1, LocalDate.of(2026, 3, 2),
            2, LocalDate.of(2026, 5, 11),
            3, LocalDate.of(2026, 6, 29),
            4, LocalDate.of(2026, 8, 3),
            5, LocalDate.of(2026, 9, 21),
            6, LocalDate.of(2026, 11, 9)
    );

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (taskRepository.count() > 0) {
            log.info("Tarefas já existem na base de dados. Seed ignorado.");
            return;
        }

        log.info("A iniciar seed de 204 tarefas...");

        ClassPathResource resource = new ClassPathResource("all_tasks.json");
        InputStream is = resource.getInputStream();

        Map<String, List<Map<String, Object>>> allTasks = objectMapper.readValue(
                is, new TypeReference<>() {});

        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM");
        int globalSort = 0;

        for (Map.Entry<String, List<Map<String, Object>>> entry : allTasks.entrySet()) {
            int sprintNumber = Integer.parseInt(entry.getKey());
            Sprint sprint = sprintRepository.findBySprintNumber(sprintNumber)
                    .orElseThrow(() -> new RuntimeException("Sprint " + sprintNumber + " não encontrado"));

            LocalDate sprintStart = SPRINT_START_DATES.get(sprintNumber);
            int taskIndex = 0;

            for (Map<String, Object> taskData : entry.getValue()) {
                taskIndex++;
                globalSort++;

                String dateStr = (String) taskData.get("date");
                // Parse dd/MM and add year 2026
                String[] parts = dateStr.split("/");
                int day = Integer.parseInt(parts[0]);
                int month = Integer.parseInt(parts[1]);
                LocalDate sessionDate = LocalDate.of(2026, month, day);

                String dayOfWeek = (String) taskData.get("day");
                int hours = taskData.get("hours") instanceof Integer
                        ? (Integer) taskData.get("hours")
                        : ((Number) taskData.get("hours")).intValue();
                String title = (String) taskData.get("title");

                @SuppressWarnings("unchecked")
                List<String> bullets = (List<String>) taskData.get("bullets");
                String coverage = (String) taskData.get("coverage");
                String validation = (String) taskData.get("validation");

                // Calculate week number within sprint
                int weekNumber = (int) (ChronoUnit.WEEKS.between(sprintStart, sessionDate)) + 1;
                if (weekNumber < 1) weekNumber = 1;

                String taskCode = String.format("S%d-%02d", sprintNumber, taskIndex);

                Task task = Task.builder()
                        .sprint(sprint)
                        .taskCode(taskCode)
                        .sessionDate(sessionDate)
                        .dayOfWeek(dayOfWeek)
                        .weekNumber(weekNumber)
                        .plannedHours(BigDecimal.valueOf(hours))
                        .title(title)
                        .deliverables(objectMapper.writeValueAsString(bullets))
                        .validationCriteria(objectMapper.writeValueAsString(
                                validation != null ? List.of(validation) : List.of()))
                        .coverageTarget(coverage != null ? coverage : "N/A")
                        .status(TaskStatus.PLANNED)
                        .sortOrder(globalSort)
                        .build();

                taskRepository.save(task);
            }

            log.info("Sprint {} carregado: {} tarefas", sprintNumber, taskIndex);
        }

        log.info("Seed concluído: {} tarefas inseridas.", taskRepository.count());
    }
}
