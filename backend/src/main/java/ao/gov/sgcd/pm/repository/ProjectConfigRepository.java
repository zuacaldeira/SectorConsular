package ao.gov.sgcd.pm.repository;

import ao.gov.sgcd.pm.entity.ProjectConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectConfigRepository extends JpaRepository<ProjectConfig, String> {
}
