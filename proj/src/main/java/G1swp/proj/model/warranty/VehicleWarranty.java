package G1swp.proj.model.warranty;

import G1swp.proj.model.core.User;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "vehicle_warranties", schema = "ev_warranty_db")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class VehicleWarranty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warranty_policy_id")
    private WarrantyPolicy policy;

    private LocalDate warrantyStartDate;
    private LocalDate warrantyEndDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_user_id")
    private User createdBy;

    private Boolean isActive = true;
    private LocalDateTime createdAt = LocalDateTime.now();
}
