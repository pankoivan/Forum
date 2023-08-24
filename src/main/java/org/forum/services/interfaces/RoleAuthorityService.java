package org.forum.services.interfaces;

import org.forum.entities.Authority;
import org.forum.entities.Role;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

public interface RoleAuthorityService {

    void fillModel(Model model, Authentication authentication);

    void fillModelForRoleEditing(Model model, Authentication authentication, Integer roleId);

    void fillModelForAuthorityEditing(Model model, Authentication authentication, Integer authorityId);

    void fillModelForRoleErrors(Model model, Authentication authentication, Role role,
                                BindingResult bindingResult);

    void fillModelForAuthorityErrors(Model model, Authentication authentication, Authority authority,
                                     BindingResult bindingResult);

    void saveRole(Role role);

    void deleteRoleById(Integer id);

    void saveAuthority(Authority authority);

    void deleteAuthorityById(Integer id);

}
