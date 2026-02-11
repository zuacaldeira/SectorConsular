package ao.gov.sgcd.pm.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sprints")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Sprint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sprint_number", nullable = false, unique = true)
    private Integer sprintNumber;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(name = "name_en", nullable = false, length = 120)
    private String nameEn;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Integer weeks;

    @Column(name = "total_hours", nullable = false)
    private Integer totalHours;

    @Column(name = "total_sessions", nullable = false)
    private Integer totalSessions;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(length = 60)
    private String focus;

    @Column(length = 7)
    private String color;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SprintStatus status;

    @Column(name = "actual_hours", precision = 6, scale = 1)
    private BigDecimal actualHours;

    @Column(name = "completed_sessions")
    private Integer completedSessions;

    @Column(name = "completion_notes", columnDefinition = "TEXT")
    private String completionNotes;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "sprint", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Task> tasks = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (actualHours == null) actualHours = BigDecimal.ZERO;
        if (completedSessions == null) completedSessions = 0;
        if (status == null) status = SprintStatus.PLANNED;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
