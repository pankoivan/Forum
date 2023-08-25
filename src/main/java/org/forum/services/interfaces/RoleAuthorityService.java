package org.forum.services.interfaces;

import org.forum.entities.Authority;
import org.forum.entities.Role;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface RoleAuthorityService {

    boolean roleFullValidation(Role role, BindingResult bindingResult);

    boolean roleDeletingValidation(Role role);

    boolean authorityFullValidation(Authority authority, BindingResult bindingResult);

    boolean authorityDeletingValidation(Authority authority);

    String extractAnySingleError(BindingResult bindingResult);

    Role newRole();

    boolean isNewRole(Role role);

    Role findRoleById(Integer id);

    List<Role> findAllRoles();

    void saveRole(Role role);

    void deleteRoleById(Integer id);

    Authority newAuthority();

    boolean isNewAuthority(Authority authority);

    Authority findAuthorityById(Integer id);

    List<Authority> findAllAuthorities();

    void saveAuthority(Authority authority);

    void deleteAuthorityById(Integer id);

}
