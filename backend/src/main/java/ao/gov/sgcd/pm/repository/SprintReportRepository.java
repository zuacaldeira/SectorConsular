package ao.gov.sgcd.pm.repository;

import ao.gov.sgcd.pm.entity.SprintReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SprintReportRepository extends JpaRepository<SprintReport, Long> {

    List<SprintReport> findBySprintIdOrderByGeneratedAtDesc(Long sprintId);

    List<SprintReport> findAllByOrderByGeneratedAtDesc();
}
