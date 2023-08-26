package org.forum.services.implementations;

import org.forum.entities.Role;
import org.forum.repositories.RoleRepository;
import org.forum.services.interfaces.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository repository;

    @Autowired
    public RoleServiceImpl(RoleRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean savingValidation(Role role, BindingResult bindingResult) {
        if (isNew(role) && repository.existsByName(role.getName())) {
            bindingResult.addError(new ObjectError("existsByName",
                    "Роль с таким названием уже существует"));
            return true;
        }
        return bindingResult.hasErrors();
    }

    @Override
    public String deletingValidation(Role role) {
        return role.hasUsers()
                ? "Роль \"" + role.getName() + "\" нельзя удалять, так как она присвоена каким-то пользователям"
                : null;
    }

    @Override
    public Role empty() {
        return new Role();
    }

    @Override
    public boolean isNew(Role role) {
        return role.getId() == null || !repository.existsById(role.getId());
    }

    @Override
    public Role findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role with id \"" + id + "\" doesn't exists"));
    }

    @Override
    public List<Role> findAll() {
        return repository.findAll();
    }

    @Override
    public void save(Role role) {
        repository.save(role);
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

}
