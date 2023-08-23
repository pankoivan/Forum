package org.forum.services.interfaces;

import org.forum.entities.Authority;
import org.forum.entities.Role;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;

import java.util.List;

public interface RoleAuthorityService {

    void fillRolesAuthoritiesPage(Model model, Authentication authentication);

    void fillRolesAuthoritiesPageForAuthorityEditing(Model model, Authentication authentication, Integer authorityId);

    void fillRolesAuthoritiesPageForRoleEditing(Model model, Authentication authentication, Integer roleId);

    void saveAuthority(Authority authority);

    void deleteAuthorityById(Integer id);

    void saveRole(Role role, List<Authority> authorities);

    void deleteRoleById(Integer id);

}
