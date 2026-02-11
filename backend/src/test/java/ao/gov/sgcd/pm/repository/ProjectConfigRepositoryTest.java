package ao.gov.sgcd.pm.repository;

import ao.gov.sgcd.pm.entity.ProjectConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ProjectConfigRepositoryTest {

    @Autowired
    private ProjectConfigRepository projectConfigRepository;

    @BeforeEach
    void setUp() {
        projectConfigRepository.deleteAll();
    }

    // ---- Helper Methods ----

    private ProjectConfig createConfig(String key, String value, String description) {
        ProjectConfig config = ProjectConfig.builder()
                .configKey(key)
                .configValue(value)
                .description(description)
                .build();
        return projectConfigRepository.save(config);
    }

    // ---- Basic CRUD operations ----

    @Test
    void save_shouldPersistConfigWithStringKey() {
        ProjectConfig config = ProjectConfig.builder()
                .configKey("project.name")
                .configValue("SGCD-PM")
                .description("Nome do projecto")
                .build();

        ProjectConfig saved = projectConfigRepository.save(config);

        assertThat(saved.getConfigKey()).isEqualTo("project.name");
        assertThat(saved.getConfigValue()).isEqualTo("SGCD-PM");
        assertThat(saved.getDescription()).isEqualTo("Nome do projecto");
        assertThat(saved.getUpdatedAt()).isNotNull();
    }

    @Test
    void findById_shouldReturnConfigByStringKey() {
        createConfig("project.version", "1.0.0", "Versao do projecto");

        Optional<ProjectConfig> found = projectConfigRepository.findById("project.version");

        assertThat(found).isPresent();
        assertThat(found.get().getConfigValue()).isEqualTo("1.0.0");
        assertThat(found.get().getDescription()).isEqualTo("Versao do projecto");
    }

    @Test
    void findById_shouldReturnEmptyForNonexistentKey() {
        Optional<ProjectConfig> found = projectConfigRepository.findById("nonexistent.key");

        assertThat(found).isEmpty();
    }

    @Test
    void save_shouldUpdateExistingConfig() {
        ProjectConfig saved = createConfig("current.sprint", "1", "Sprint actual");

        saved.setConfigValue("2");
        saved.setDescription("Sprint actual atualizado");
        projectConfigRepository.save(saved);

        Optional<ProjectConfig> found = projectConfigRepository.findById("current.sprint");
        assertThat(found).isPresent();
        assertThat(found.get().getConfigValue()).isEqualTo("2");
        assertThat(found.get().getDescription()).isEqualTo("Sprint actual atualizado");
    }

    @Test
    void delete_shouldRemoveConfig() {
        ProjectConfig saved = createConfig("temp.key", "temp-value", "Temporario");

        projectConfigRepository.delete(saved);

        Optional<ProjectConfig> found = projectConfigRepository.findById("temp.key");
        assertThat(found).isEmpty();
    }

    @Test
    void deleteById_shouldRemoveConfigByKey() {
        createConfig("delete.me", "value", "To be deleted");

        projectConfigRepository.deleteById("delete.me");

        Optional<ProjectConfig> found = projectConfigRepository.findById("delete.me");
        assertThat(found).isEmpty();
    }

    @Test
    void findAll_shouldReturnAllConfigs() {
        createConfig("key1", "value1", "Description 1");
        createConfig("key2", "value2", "Description 2");
        createConfig("key3", "value3", "Description 3");

        List<ProjectConfig> result = projectConfigRepository.findAll();

        assertThat(result).hasSize(3);
    }

    @Test
    void count_shouldReturnCorrectCount() {
        createConfig("key1", "value1", "Desc 1");
        createConfig("key2", "value2", "Desc 2");

        long count = projectConfigRepository.count();

        assertThat(count).isEqualTo(2);
    }

    @Test
    void existsById_shouldReturnTrueForExistingKey() {
        createConfig("exists.key", "value", "Exists");

        boolean exists = projectConfigRepository.existsById("exists.key");

        assertThat(exists).isTrue();
    }

    @Test
    void existsById_shouldReturnFalseForNonexistentKey() {
        boolean exists = projectConfigRepository.existsById("no.such.key");

        assertThat(exists).isFalse();
    }

    @Test
    void save_shouldHandleLargeConfigValue() {
        String largeValue = "A".repeat(5000);
        ProjectConfig saved = createConfig("large.value.key", largeValue, "Large value test");

        Optional<ProjectConfig> found = projectConfigRepository.findById("large.value.key");

        assertThat(found).isPresent();
        assertThat(found.get().getConfigValue()).isEqualTo(largeValue);
        assertThat(found.get().getConfigValue()).hasSize(5000);
    }

    @Test
    void save_shouldSetUpdatedAtOnPersist() {
        ProjectConfig config = ProjectConfig.builder()
                .configKey("timestamp.test")
                .configValue("test")
                .description("Timestamp test")
                .build();

        ProjectConfig saved = projectConfigRepository.save(config);

        assertThat(saved.getUpdatedAt()).isNotNull();
    }

    @Test
    void save_shouldHandleMultipleConfigsWithDifferentKeyFormats() {
        createConfig("simple", "value1", "Simple key");
        createConfig("dotted.key", "value2", "Dotted key");
        createConfig("key-with-dashes", "value3", "Dashed key");
        createConfig("key_with_underscores", "value4", "Underscore key");

        long count = projectConfigRepository.count();

        assertThat(count).isEqualTo(4);
    }

    @Test
    void save_shouldAllowNullDescription() {
        ProjectConfig config = ProjectConfig.builder()
                .configKey("no.desc")
                .configValue("value")
                .build();

        ProjectConfig saved = projectConfigRepository.save(config);

        Optional<ProjectConfig> found = projectConfigRepository.findById("no.desc");
        assertThat(found).isPresent();
        assertThat(found.get().getDescription()).isNull();
        assertThat(found.get().getConfigValue()).isEqualTo("value");
    }
}
