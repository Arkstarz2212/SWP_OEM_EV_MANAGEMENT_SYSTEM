package G1swp.proj.controller.auth;

import G1swp.proj.dto.user.LogIn;
import G1swp.proj.dto.user.SignUp;
import G1swp.proj.model.Role;
import G1swp.proj.model.User;
import G1swp.proj.repository.auth.RoleRepository;
import G1swp.proj.repository.user.UserRepository;
import G1swp.proj.service.jwt.JWTService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private static final String ERROR_MESSAGE = "Invalid Username or Password";

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JWTService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Operation(
            summary = "User Signup",
            description = "Creates a new user and returns a JWT token upon successful registration."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Token generated successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Map.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Username already existed or role missing",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    @PostMapping(
            value = "/register",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Map<String, String>> signup(@RequestBody SignUp signupRequest) {
        // Validate username
        if (signupRequest == null || signupRequest.getUsername() == null) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Invalid signup data"));
        }

        if (userRepository.findByUsernameIgnoreCase(signupRequest.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Username already existed"));
        }

        Role userRole = roleRepository.findByName("user")
                .orElseThrow(() -> new RuntimeException("Default role 'user' not found"));

        User user = new User();
        user.setUsername(signupRequest.getUsername());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        user.setFullName(signupRequest.getFullName());
        user.setEmail(signupRequest.getEmail());
        user.setDob(signupRequest.getDob());
        user.setSex(signupRequest.getSex());
        user.setPhoneNumber(signupRequest.getPhoneNumber());
        user.setRole(userRole);

        user = userRepository.save(user);

        String token = jwtService.generateToken(user.getId());
        Map<String, String> responseBody = Collections.singletonMap("token", token);
        return ResponseEntity.ok(responseBody);
    }

    @Operation(
            summary = "User Login",
            description = "Authenticates a user and returns a JWT token if the credentials are valid."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Login successful, returns JWT token and user info",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Map.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Invalid username or password",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
    })
    @PostMapping(
            value = "/login",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Map<String, Object>> login(@RequestBody LogIn loginRequest) {
        if (loginRequest == null || loginRequest.getUsername() == null) {
            return ResponseEntity.status(401).body(Collections.singletonMap("error", ERROR_MESSAGE));
        }
        Optional<User> userOptional = userRepository
                .findByUsernameIgnoreCase(loginRequest.getUsername());

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(401).body(Collections.singletonMap("error", ERROR_MESSAGE));
        }

        User user = userOptional.get();

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body(Collections.singletonMap("error", ERROR_MESSAGE));
        }

        String token = jwtService.generateToken(user.getId());

        Map<String, Object> userPayload = new HashMap<>();
        userPayload.put("id", user.getId());
        userPayload.put("full_name", user.getFullName());
        userPayload.put("avatar_path", user.getAvtarPath());
        userPayload.put("role", user.getRole() != null ? user.getRole().getName() : null);

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("token", token);
        responseBody.put("user", userPayload);

        return ResponseEntity.ok(responseBody);
    }
}
