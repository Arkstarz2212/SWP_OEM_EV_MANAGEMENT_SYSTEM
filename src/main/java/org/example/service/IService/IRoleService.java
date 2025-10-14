package org.example.service.IService;

import java.util.List;

import org.example.models.core.Role;

public interface IRoleService {
    Role create(Role role);

    Role update(Role role);

    void delete(Long id);

    Role getById(Long id);

    Role getByCode(String code);

    List<Role> listAll();
}
