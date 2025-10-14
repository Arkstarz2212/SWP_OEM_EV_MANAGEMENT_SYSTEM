package org.example.service.Serviceimpl;

import java.util.List;

import org.example.models.core.Role;
import org.example.repository.IRepository.IRoleRepository;
import org.example.service.IService.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService implements IRoleService {

    @Autowired
    private IRoleRepository roleRepository;

    @Override
    public Role create(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public Role update(Role role) {
        return roleRepository.update(role);
    }

    @Override
    public void delete(Long id) {
        roleRepository.deleteById(id);
    }

    @Override
    public Role getById(Long id) {
        return roleRepository.findById(id).orElse(null);
    }

    @Override
    public Role getByCode(String code) {
        return roleRepository.findByCode(code).orElse(null);
    }

    @Override
    public List<Role> listAll() {
        return roleRepository.findAll();
    }
}
