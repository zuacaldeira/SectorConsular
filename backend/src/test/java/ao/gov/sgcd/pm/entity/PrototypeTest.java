package ao.gov.sgcd.pm.entity;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PrototypeTest {

    @Test
    void builder_shouldCreatePrototypeWithAllFields() {
        LocalDateTime createdAt = LocalDateTime.now();

        Prototype prototype = Prototype.builder()
                .id(1L)
                .name("Dashboard Consular")
                .module("frontend")
                .filePath("/prototypes/dashboard.html")
                .fileType("html")
                .description("Prototype for consular dashboard")
                .createdAt(createdAt)
                .build();

        assertEquals(1L, prototype.getId());
        assertEquals("Dashboard Consular", prototype.getName());
        assertEquals("frontend", prototype.getModule());
        assertEquals("/prototypes/dashboard.html", prototype.getFilePath());
        assertEquals("html", prototype.getFileType());
        assertEquals("Prototype for consular dashboard", prototype.getDescription());
        assertEquals(createdAt, prototype.getCreatedAt());
    }

    @Test
    void gettersAndSetters_shouldWorkForAllFields() {
        Prototype prototype = new Prototype();
        LocalDateTime createdAt = LocalDateTime.now();

        prototype.setId(2L);
        prototype.setName("API Schema");
        prototype.setModule("backend");
        prototype.setFilePath("/prototypes/api-schema.json");
        prototype.setFileType("json");
        prototype.setDescription("OpenAPI schema for backend");
        prototype.setCreatedAt(createdAt);

        assertEquals(2L, prototype.getId());
        assertEquals("API Schema", prototype.getName());
        assertEquals("backend", prototype.getModule());
        assertEquals("/prototypes/api-schema.json", prototype.getFilePath());
        assertEquals("json", prototype.getFileType());
        assertEquals("OpenAPI schema for backend", prototype.getDescription());
        assertEquals(createdAt, prototype.getCreatedAt());
    }

    @Test
    void noArgConstructor_shouldCreateEmptyPrototype() {
        Prototype prototype = new Prototype();

        assertNull(prototype.getId());
        assertNull(prototype.getName());
        assertNull(prototype.getModule());
        assertNull(prototype.getFilePath());
        assertNull(prototype.getFileType());
        assertNull(prototype.getDescription());
        assertNull(prototype.getCreatedAt());
    }

    @Test
    void allArgsConstructor_shouldSetAllFields() {
        LocalDateTime createdAt = LocalDateTime.now();

        Prototype prototype = new Prototype(
                3L, "Wireframe Login", "auth",
                "/prototypes/login.fig", "figma",
                "Login page wireframe", createdAt
        );

        assertEquals(3L, prototype.getId());
        assertEquals("Wireframe Login", prototype.getName());
        assertEquals("auth", prototype.getModule());
        assertEquals("/prototypes/login.fig", prototype.getFilePath());
        assertEquals("figma", prototype.getFileType());
        assertEquals("Login page wireframe", prototype.getDescription());
        assertEquals(createdAt, prototype.getCreatedAt());
    }

    @Test
    void prePersist_shouldSetCreatedAt() {
        Prototype prototype = new Prototype();
        assertNull(prototype.getCreatedAt());

        prototype.onCreate();

        assertNotNull(prototype.getCreatedAt());
    }

    @Test
    void prePersist_createdAtShouldBeCurrentTime() {
        Prototype prototype = new Prototype();
        LocalDateTime before = LocalDateTime.now();

        prototype.onCreate();

        LocalDateTime after = LocalDateTime.now();
        assertNotNull(prototype.getCreatedAt());
        assertFalse(prototype.getCreatedAt().isBefore(before));
        assertFalse(prototype.getCreatedAt().isAfter(after));
    }
}
