package org.forum.services.implementations;

import org.forum.entities.Authority;
import org.forum.entities.Role;
import org.forum.repositories.AuthorityRepository;
import org.forum.repositories.RoleRepository;
import org.forum.services.interfaces.RoleAuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.List;

@Service
public class RoleAuthorityServiceImpl implements RoleAuthorityService {

    private final RoleRepository roleRepository;

    private final AuthorityRepository authorityRepository;

    @Autowired
    public RoleAuthorityServiceImpl(RoleRepository roleRepository, AuthorityRepository authorityRepository) {
        this.roleRepository = roleRepository;
        this.authorityRepository = authorityRepository;
    }

    @Override
    public boolean roleFullValidation(Role role, BindingResult bindingResult) {
        if (roleRepository.existsByName(role.getName()) && isNewRole(role)) {
            bindingResult.addError(new ObjectError("roleExistsByName",
                    "Роль с таким названием уже существует"));
            return true;
        }
        return bindingResult.hasErrors();
    }

    @Override
    public boolean roleDeletingValidation(Role role) {
        return role.hasUsers();
    }

    @Override
    public boolean authorityFullValidation(Authority authority, BindingResult bindingResult) {
        if (authorityRepository.existsByName(authority.getName()) && isNewAuthority(authority)) {
            bindingResult.addError(new ObjectError("authorityExistsByName",
                    "Право с таким названием уже существует"));
            return true;
        }
        return bindingResult.hasErrors();
    }

    @Override
    public boolean authorityDeletingValidation(Authority authority) {
        return authority.getRoles().stream()
                .anyMatch(role -> role.getAuthorities().size() == 1);
    }

    @Override
    public String extractAnySingleError(BindingResult bindingResult) {
        return bindingResult.getAllErrors().stream()
                .findAny()
                .orElseThrow(() -> new RuntimeException("There was no errors in BindingResult"))
                .getDefaultMessage();
    }

    @Override
    public Role newRole() {
        return new Role();
    }

    @Override
    public boolean isNewRole(Role role) {
        return role.getId() == null || !roleRepository.existsById(role.getId());
    }

    @Override
    public Role findRoleById(Integer id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role with id \"" + id + "\" doesn't exists"));
    }

    @Override
    public List<Role> findAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public void saveRole(Role role) {
        roleRepository.save(role);
    }

    @Override
    public void deleteRoleById(Integer id) {
        roleRepository.deleteById(id);
    }

    @Override
    public Authority newAuthority() {
        return new Authority();
    }

    @Override
    public boolean isNewAuthority(Authority authority) {
        return authority.getId() == null || !authorityRepository.existsById(authority.getId());
    }

    @Override
    public Authority findAuthorityById(Integer id) {
        return authorityRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Authority with id \"" + id + "\" doesn't exists"));
    }

    @Override
    public List<Authority> findAllAuthorities() {
        return authorityRepository.findAll();
    }

    @Override
    public void saveAuthority(Authority authority) {
        authorityRepository.save(authority);
    }

    @Override
    public void deleteAuthorityById(Integer id) {
        Authority authority = findAuthorityById(id);
        authority.getRoles()
                .forEach(role -> role.removeAuthority(authority));
        authorityRepository.deleteById(id);
    }

}
