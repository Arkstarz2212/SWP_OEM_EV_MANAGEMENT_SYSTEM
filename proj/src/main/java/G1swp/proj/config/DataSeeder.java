package G1swp.proj.config;

import G1swp.proj.model.core.Role;
import G1swp.proj.model.core.User;
import G1swp.proj.repository.auth.RoleRepository;
import G1swp.proj.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Slf4j
@Component
@Order(2) // Run after DatabaseConnectionConfig
@RequiredArgsConstructor
public class DataSeeder implements ApplicationRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        log.info("Starting data seeding process...");

        // Check if roles exist
        long roleCount = roleRepository.count();
        if (roleCount == 0) {
            log.info("No roles found in database. Seeding default roles...");
            seedRoles();
        } else {
            log.info("Roles already exist in database (count: {}). Skipping role seeding.", roleCount);
        }

        // Check if admin account exists
        if (userRepository.findByUsernameIgnoreCase("admin").isEmpty()) {
            log.info("No admin account found. Creating default admin account...");
            seedAdminAccount();
        } else {
            log.info("Admin account already exists. Skipping admin account creation.");
        }

        log.info("Data seeding process completed.");
    }

    private void seedRoles() {
        try {
            // Create admin role (level 100 - highest)
            Role adminRole = new Role();
            adminRole.setName("admin");
            adminRole.setLevel(100);
            adminRole = roleRepository.save(adminRole);
            log.info("✓ Created role: {} (level: {})", adminRole.getName(), adminRole.getLevel());

            // Create staff role (level 50 - medium)
            Role staffRole = new Role();
            staffRole.setName("staff");
            staffRole.setLevel(50);
            staffRole = roleRepository.save(staffRole);
            log.info("✓ Created role: {} (level: {})", staffRole.getName(), staffRole.getLevel());

            // Create user role (level 1 - lowest)
            Role userRole = new Role();
            userRole.setName("user");
            userRole.setLevel(1);
            userRole = roleRepository.save(userRole);
            log.info("✓ Created role: {} (level: {})", userRole.getName(), userRole.getLevel());

            log.info("✓ Successfully seeded {} default roles", roleRepository.count());
        } catch (Exception e) {
            log.error("✗ Failed to seed roles", e);
            throw new RuntimeException("Failed to seed roles", e);
        }
    }

    private void seedAdminAccount() {
        try {
            // Find admin role
            Role adminRole = roleRepository.findByName("admin")
                    .orElseThrow(() -> new RuntimeException("Admin role not found. Please seed roles first."));

            // Create admin user
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("Admin@123")); // Default password
            admin.setFullName("System Administrator");
            admin.setEmail("admin@ev-warranty.com");
            admin.setPhoneNumber("0000000000");
            admin.setDob(LocalDate.of(1990, 1, 1));
            admin.setSex("Male");
            admin.setRole(adminRole);

            admin = userRepository.save(admin);
            log.info("✓ Created admin account:");
            log.info("  - Username: admin");
            log.info("  - Password: Admin@123 (CHANGE THIS IMMEDIATELY!)");
            log.info("  - Role: {} (level: {})", adminRole.getName(), adminRole.getLevel());
            log.warn("⚠ SECURITY WARNING: Default admin password is 'Admin@123'. Please change it immediately!");
        } catch (Exception e) {
            log.error("✗ Failed to create admin account", e);
            throw new RuntimeException("Failed to create admin account", e);
        }
    }
}

