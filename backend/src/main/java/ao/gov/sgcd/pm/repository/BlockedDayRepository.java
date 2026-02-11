package ao.gov.sgcd.pm.repository;

import ao.gov.sgcd.pm.entity.BlockedDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface BlockedDayRepository extends JpaRepository<BlockedDay, Long> {

    Optional<BlockedDay> findByBlockedDate(LocalDate date);

    @Query("SELECT b FROM BlockedDay b WHERE b.blockedDate >= :date ORDER BY b.blockedDate ASC")
    List<BlockedDay> findUpcoming(@Param("date") LocalDate date);

    @Query("SELECT b FROM BlockedDay b WHERE b.blockedDate BETWEEN :from AND :to ORDER BY b.blockedDate ASC")
    List<BlockedDay> findByDateRange(@Param("from") LocalDate from, @Param("to") LocalDate to);
}
