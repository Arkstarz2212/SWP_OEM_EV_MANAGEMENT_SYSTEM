package G1swp.proj.dto.user;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class SignUp {
    @Schema(
            description = "User's login username",
            example = "john_doe"
    )
    private String username;

    @Schema(
            description = "User's password",
            example = "P@ssw0rd!"
    )
    private String password;

    @Schema(
            description ="Full name of the user",
            example = "John Doe"
    )
    private String fullName;

    @Schema(
            description = "full email of the user",
            example = "email@gmail.com"
    )
    private String email;

    private String phoneNumber;
    private LocalDate dob;
    private String sex;
}
