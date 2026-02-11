package ao.gov.sgcd.pm.repository;

import ao.gov.sgcd.pm.entity.TaskExecution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskExecutionRepository extends JpaRepository<TaskExecution, Long> {

    List<TaskExecution> findByTaskIdOrderByStartedAtDesc(Long taskId);
}
