package G1swp.proj.model.warranty;

import G1swp.proj.model.oem.OEMManufacturer;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "warranty_policies", schema = "ev_warranty_db")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class WarrantyPolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "oem_id")
    private OEMManufacturer oem;

    @Column(nullable = false)
    private String policyName;

    @Column(unique = true, nullable = false)
    private String policyCode;

    private Integer warrantyMonths;
    private Integer warrantyKm;
    private Integer batteryCoverageMonths;
    private Integer motorCoverageMonths;
    private Integer inverterCoverageMonths;
    private Boolean isDefault;
    private Boolean isActive;
}
