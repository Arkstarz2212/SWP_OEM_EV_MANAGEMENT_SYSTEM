package G1swp.proj.model.servicecenter;

import G1swp.proj.model.oem.OEMManufacturer;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "service_centers", schema = "ev_warranty_db")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ServiceCenter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String code;

    @Column(nullable = false)
    private String name;

    private String region;
    private String location;

    @Column(columnDefinition = "jsonb")
    private String contactInfo;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "service_center_status DEFAULT 'OPEN'")
    private Status status = Status.OPEN;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "oem_id")
    private OEMManufacturer oem;

    private Boolean active = true;

    public enum Status { OPEN, CLOSED, SUSPENDED }
}
