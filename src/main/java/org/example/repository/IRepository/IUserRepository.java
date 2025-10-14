package org.example.repository.IRepository;

import java.util.List;
import java.util.Optional;

import org.example.models.core.User;
import org.example.models.enums.UserRole;

public interface IUserRepository {
    // Basic CRUD operations
    User save(User user);

    Optional<User> findById(Long id);

    List<User> findAll();

    void deleteById(Long id);

    boolean existsById(Long id);

    // Authentication related methods
    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByPhoneNumber(String phoneNumber);

    Optional<User> findByPasswordResetToken(String resetToken);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    // User management methods
    List<User> findByRole(UserRole role);

    List<User> findByServiceCenterId(Long serviceCenterId);

    List<User> findByIsActive(Boolean isActive);

    List<User> findByRoleAndServiceCenterId(UserRole role, Long serviceCenterId);

    // Search and filter methods
    List<User> searchUsers(String keyword);

    List<User> findByFullNameContainingIgnoreCase(String fullName);

    // Statistics methods
    Long countByRole(UserRole role);

    Long countByIsActive(Boolean isActive);

    Long countByServiceCenterId(Long serviceCenterId);
}