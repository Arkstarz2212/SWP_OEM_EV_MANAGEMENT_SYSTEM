package G1swp.proj.model.oem;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vehicle_models", schema = "ev_warranty_db")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class VehicleModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String modelCode;

    @Column(nullable = false)
    private String modelName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manufacturer_id")
    private OEMManufacturer manufacturer;

    private Double batteryCapacity;
    private Integer rangeKm;
    private Integer modelYear;
    private String description;
}
