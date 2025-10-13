package org.example.repository.IRepository;

import java.util.List;
import java.util.Optional;

import org.example.models.core.Role;

public interface IRoleRepository {
    Role save(Role role);

    Role update(Role role);

    void deleteById(Long id);

    Optional<Role> findById(Long id);

    Optional<Role> findByCode(String code);

    List<Role> findAll();
}
