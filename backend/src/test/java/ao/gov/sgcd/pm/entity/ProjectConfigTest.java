package ao.gov.sgcd.pm.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ProjectConfigTest {

    @Test
    void builder_shouldCreateProjectConfigWithAllFields() {
        LocalDateTime updatedAt = LocalDateTime.now();

        ProjectConfig config = ProjectConfig.builder()
                .configKey("project.name")
                .configValue("SGCD-PM")
                .description("Project name configuration")
                .updatedAt(updatedAt)
                .build();

        assertEquals("project.name", config.getConfigKey());
        assertEquals("SGCD-PM", config.getConfigValue());
        assertEquals("Project name configuration", config.getDescription());
        assertEquals(updatedAt, config.getUpdatedAt());
    }

    @Test
    void gettersAndSetters_shouldWorkForAllFields() {
        ProjectConfig config = new ProjectConfig();
        LocalDateTime updatedAt = LocalDateTime.now();

        config.setConfigKey("sprint.current");
        config.setConfigValue("1");
        config.setDescription("Current active sprint number");
        config.setUpdatedAt(updatedAt);

        assertEquals("sprint.current", config.getConfigKey());
        assertEquals("1", config.getConfigValue());
        assertEquals("Current active sprint number", config.getDescription());
        assertEquals(updatedAt, config.getUpdatedAt());
    }

    @Test
    void noArgConstructor_shouldCreateEmptyProjectConfig() {
        ProjectConfig config = new ProjectConfig();

        assertNull(config.getConfigKey());
        assertNull(config.getConfigValue());
        assertNull(config.getDescription());
        assertNull(config.getUpdatedAt());
    }

    @Test
    void allArgsConstructor_shouldSetAllFields() {
        LocalDateTime updatedAt = LocalDateTime.now();

        ProjectConfig config = new ProjectConfig(
                "total.hours", "680",
                "Total planned hours", updatedAt
        );

        assertEquals("total.hours", config.getConfigKey());
        assertEquals("680", config.getConfigValue());
        assertEquals("Total planned hours", config.getDescription());
        assertEquals(updatedAt, config.getUpdatedAt());
    }

    @Test
    void onSave_shouldSetUpdatedAt() {
        ProjectConfig config = new ProjectConfig();
        assertNull(config.getUpdatedAt());

        config.onSave();

        assertNotNull(config.getUpdatedAt());
    }

    @Test
    void onSave_shouldRefreshUpdatedAt() {
        ProjectConfig config = new ProjectConfig();
        LocalDateTime oldTime = LocalDateTime.of(2025, 1, 1, 0, 0);
        config.setUpdatedAt(oldTime);

        config.onSave();

        assertNotNull(config.getUpdatedAt());
        assertTrue(config.getUpdatedAt().isAfter(oldTime));
    }

    @Test
    void onSave_updatedAtShouldBeCurrentTime() {
        ProjectConfig config = new ProjectConfig();
        LocalDateTime before = LocalDateTime.now();

        config.onSave();

        LocalDateTime after = LocalDateTime.now();
        assertNotNull(config.getUpdatedAt());
        assertFalse(config.getUpdatedAt().isBefore(before));
        assertFalse(config.getUpdatedAt().isAfter(after));
    }

    @Test
    void configKey_isTheId() {
        // configKey serves as the @Id, not a generated Long
        ProjectConfig config = ProjectConfig.builder()
                .configKey("my.key")
                .configValue("my.value")
                .build();

        assertEquals("my.key", config.getConfigKey());
    }
}
