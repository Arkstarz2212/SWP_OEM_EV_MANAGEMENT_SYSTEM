package G1swp.proj.model.oem;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "oem_manufacturers", schema = "ev_warranty_db")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class OEMManufacturer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false, length = 10)
    private String code;

    @Column(nullable = false)
    private String name;

    private String country;

    @Column(columnDefinition = "jsonb")
    private String contact;
}
