package org.forum.services.implementations;

import org.forum.entities.Authority;
import org.forum.entities.Role;
import org.forum.repositories.AuthorityRepository;
import org.forum.repositories.RoleRepository;
import org.forum.services.interfaces.RoleAuthorityService;
import org.forum.utils.AuthenticationUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
public class RoleAuthorityServiceImpl implements RoleAuthorityService {

    private final RoleRepository roleRepository;

    private final AuthorityRepository authorityRepository;

    public RoleAuthorityServiceImpl(RoleRepository roleRepository, AuthorityRepository authorityRepository) {
        this.roleRepository = roleRepository;
        this.authorityRepository = authorityRepository;
    }

    @Override
    public void fillRolesAuthoritiesPage(Model model, Authentication authentication) {
        fillRolesAuthoritiesPageWithBasicContent(model, authentication);
        fillRolesAuthoritiesPageWithEmptyRoleAndEmptyAuthority(model);
    }

    @Override
    public void fillRolesAuthoritiesPageForAuthorityEditing(Model model, Authentication authentication, Integer authorityId) {
        fillRolesAuthoritiesPageWithBasicContent(model, authentication);
        fillRolesAuthoritiesPageWithEmptyRoleAndSpecifiedAuthority(model, authorityId);
    }

    @Override
    public void fillRolesAuthoritiesPageForRoleEditing(Model model, Authentication authentication, Integer roleId) {
        fillRolesAuthoritiesPageWithBasicContent(model, authentication);
        fillRolesAuthoritiesPageWithSpecifiedRoleAndEmptyAuthority(model, roleId);
    }

    @Override
    public void saveAuthority(Authority authority) {
        authorityRepository.save(authority);
    }

    @Override
    public void deleteAuthorityById(Integer id) {
        authorityRepository.deleteById(id);
    }

    @Override
    public void saveRole(Role role, List<Authority> authorities) {
        role.clearAuthorities();
        role.addAuthorities(authorities);
        roleRepository.save(role);
    }

    @Override
    public void deleteRoleById(Integer id) {
        roleRepository.deleteById(id);
    }

    private void fillRolesAuthoritiesPageWithBasicContent(Model model, Authentication authentication) {
        model.addAttribute("currentUser", AuthenticationUtils.extractCurrentUserOrNull(authentication));
        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("authorities", authorityRepository.findAll());
    }

    private void fillRolesAuthoritiesPageWithRoleAndAuthority(Model model, Role role, Authority authority) {
        model.addAttribute("role", role);
        model.addAttribute("authority", authority);
    }

    private void fillRolesAuthoritiesPageWithEmptyRoleAndEmptyAuthority(Model model) {
        fillRolesAuthoritiesPageWithRoleAndAuthority(model, new Role(), new Authority());
    }

    private void fillRolesAuthoritiesPageWithEmptyRoleAndSpecifiedAuthority(Model model, Integer authorityId) {
        fillRolesAuthoritiesPageWithRoleAndAuthority(model, new Role(), authorityRepository.findById(authorityId)
                .orElseThrow(() -> new RuntimeException("Authority with id \"" + authorityId + "\" doesn't exists")));
    }

    private void fillRolesAuthoritiesPageWithSpecifiedRoleAndEmptyAuthority(Model model, Integer roleId) {
        fillRolesAuthoritiesPageWithRoleAndAuthority(model, roleRepository.findById(roleId)
                        .orElseThrow(() -> new RuntimeException("Role with id \"" + roleId + "\" doesn't exists")),
                new Authority());
    }

}
