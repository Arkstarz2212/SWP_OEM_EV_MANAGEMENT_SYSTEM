package G1swp.proj.dto.role;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto {

    @Schema(description = "Role id")
    private UUID id;

    @Schema(description = "Role name", example = "user")
    private String name;

    @Schema(description = "Role level", example = "1")
    private int level;
}

