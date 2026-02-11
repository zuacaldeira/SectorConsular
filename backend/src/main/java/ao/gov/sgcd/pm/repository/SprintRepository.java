package ao.gov.sgcd.pm.repository;

import ao.gov.sgcd.pm.entity.Sprint;
import ao.gov.sgcd.pm.entity.SprintStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SprintRepository extends JpaRepository<Sprint, Long> {

    Optional<Sprint> findBySprintNumber(Integer sprintNumber);

    Optional<Sprint> findByStatus(SprintStatus status);

    @Query("SELECT s FROM Sprint s ORDER BY s.sprintNumber ASC")
    List<Sprint> findAllOrdered();

    @Query("SELECT s FROM Sprint s WHERE s.status = 'ACTIVE'")
    Optional<Sprint> findActiveSprint();

    Optional<Sprint> findFirstByStatusOrderBySprintNumberAsc(SprintStatus status);
}
