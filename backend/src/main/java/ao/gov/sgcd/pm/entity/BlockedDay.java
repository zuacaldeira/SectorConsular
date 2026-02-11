package ao.gov.sgcd.pm.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "blocked_days")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class BlockedDay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "blocked_date", nullable = false, unique = true)
    private LocalDate blockedDate;

    @Column(name = "day_of_week", nullable = false, length = 3)
    private String dayOfWeek;

    @Enumerated(EnumType.STRING)
    @Column(name = "block_type", nullable = false)
    private BlockType blockType;

    @Column(nullable = false, length = 200)
    private String reason;

    @Column(name = "hours_lost", nullable = false, precision = 4, scale = 1)
    private BigDecimal hoursLost;
}
