package G1swp.proj.model.warranty;

import G1swp.proj.model.oem.*;
import G1swp.proj.model.core.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vehicles", schema = "ev_warranty_db")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String vin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id")
    private VehicleModel modelEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "oem_id")
    private OEMManufacturer oem;

    private String model;
    private Integer modelYear;
    private String color;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private User customer;

    @Column(columnDefinition = "jsonb")
    private String vehicleData;

    @Column(columnDefinition = "jsonb")
    private String warrantyInfo;
}
