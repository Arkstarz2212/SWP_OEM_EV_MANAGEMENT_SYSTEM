package G1swp.proj.dto.user;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;


@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LogIn {
    @Schema(
            description = "User's login username"
            , example = "john_doe"
    )
    private String username;
    @Schema(
            description = "User's password",
            example = "P@ssw0rd!"
    )
    private String password;
}
