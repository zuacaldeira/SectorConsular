package ao.gov.sgcd.pm.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "prototypes")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Prototype {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false, length = 60)
    private String module;

    @Column(name = "file_path", length = 500)
    private String filePath;

    @Column(name = "file_type", length = 10)
    private String fileType;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
