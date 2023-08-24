package org.forum.services.implementations;

import org.forum.entities.Authority;
import org.forum.entities.Role;
import org.forum.repositories.AuthorityRepository;
import org.forum.repositories.RoleRepository;
import org.forum.services.interfaces.RoleAuthorityService;
import org.forum.utils.AuthenticationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Objects;

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
    public void fillModel(Model model, Authentication authentication) {
        fillModelWithBasicContent(model, authentication);
        fillModelWithEmptyRoleAndEmptyAuthority(model);
        fillModelWithCreateRoleTextAndCreateAuthorityText(model);
    }

    @Override
    public void fillModelForRoleEditing(Model model, Authentication authentication, Integer roleId) {
        fillModelWithBasicContent(model, authentication);
        fillModelWithSpecifiedRoleAndEmptyAuthority(model, roleId);
        fillModelWithEditRoleTextAndCreateAuthorityText(model);
    }

    @Override
    public void fillModelForAuthorityEditing(Model model, Authentication authentication, Integer authorityId) {
        fillModelWithBasicContent(model, authentication);
        fillModelWithEmptyRoleAndSpecifiedAuthority(model, authorityId);
        fillModelWithCreateRoleTextAndEditAuthorityText(model);
    }

    @Override
    public void fillModelForRoleErrors(Model model,
                                       Authentication authentication,
                                       Role role,
                                       List<Authority> authorities,
                                       BindingResult bindingResult) {

        role.setAuthorities(authorities);
        fillModelWithBasicContent(model, authentication);
        fillModelWithSpecifiedRoleAndEmptyAuthority(model, role);
        if (role.getId() == null) {
            fillModelWithEditRoleTextAndCreateAuthorityText(model);
        } else {
            fillModelWithCreateRoleTextAndCreateAuthorityText(model);
        }
        fillModelWithAnySingleError(model, bindingResult, "roleError");
    }

    @Override
    public void fillModelForAuthorityErrors(Model model, Authentication authentication, Authority authority,
                                            BindingResult bindingResult) {

        fillModelWithBasicContent(model, authentication);
        fillModelWithEmptyRoleAndSpecifiedAuthority(model, authority);
        fillModelWithCreateRoleTextAndCreateAuthorityText(model);
        if (authority.getId() == null) {
            fillModelWithCreateRoleTextAndEditAuthorityText(model);
        } else {
            fillModelWithCreateRoleTextAndCreateAuthorityText(model);
        }
        fillModelWithAnySingleError(model, bindingResult, "authorityError");
    }

    @Override
    public void saveRole(Role role, List<Authority> authorities) {
        if (role.getAuthorities() == null) {
            role.setAuthorities(authorities);
        } else {
            role.clearAuthorities();
            role.addAuthorities(authorities);
        }
        roleRepository.save(role);
    }

    @Override
    public void deleteRoleById(Integer id) {
        roleRepository.deleteById(id);
    }

    @Override
    public void saveAuthority(Authority authority) {
        authorityRepository.save(authority);
    }

    @Override
    public void deleteAuthorityById(Integer id) {
        authorityRepository.deleteById(id);
    }

    private void fillModelWithBasicContent(Model model, Authentication authentication) {
        model.addAttribute("currentUser", AuthenticationUtils.extractCurrentUserOrNull(authentication));
        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("authorities", authorityRepository.findAll());
    }

    private void fillModelWithRoleAndAuthority(Model model, Role role, Authority authority) {
        model.addAttribute("role", role);
        model.addAttribute("authority", authority);
    }

    private void fillModelWithEmptyRoleAndEmptyAuthority(Model model) {
        fillModelWithRoleAndAuthority(model, new Role(), new Authority());
    }

    private void fillModelWithSpecifiedRoleAndEmptyAuthority(Model model, Integer roleId) {
        fillModelWithRoleAndAuthority(model, roleRepository.findById(roleId)
                        .orElseThrow(() -> new RuntimeException("Role with id \"" + roleId + "\" doesn't exists")),
                new Authority());
    }

    private void fillModelWithSpecifiedRoleAndEmptyAuthority(Model model, Role role) {
        fillModelWithRoleAndAuthority(model, role, new Authority());
    }

    private void fillModelWithEmptyRoleAndSpecifiedAuthority(Model model, Integer authorityId) {
        fillModelWithRoleAndAuthority(model, new Role(), authorityRepository.findById(authorityId)
                .orElseThrow(() -> new RuntimeException("Authority with id \"" + authorityId + "\" doesn't exists")));
    }

    private void fillModelWithEmptyRoleAndSpecifiedAuthority(Model model, Authority authority) {
        fillModelWithRoleAndAuthority(model, new Role(), authority);
    }

    private void fillModelWithFormSubmitButtonsTexts(Model model, String roleFormSubmitButtonText,
                                                     String authorityFormSubmitButtonText) {
        
        model.addAttribute("roleFormSubmitButtonText", roleFormSubmitButtonText);
        model.addAttribute("authorityFormSubmitButtonText", authorityFormSubmitButtonText);
    }

    private void fillModelWithCreateRoleTextAndCreateAuthorityText(Model model) {
        fillModelWithFormSubmitButtonsTexts(model,
                "Создать роль", "Создать право");
    }

    private void fillModelWithEditRoleTextAndCreateAuthorityText(Model model) {
        fillModelWithFormSubmitButtonsTexts(model,
                "Сохранить", "Создать право");
    }

    private void fillModelWithCreateRoleTextAndEditAuthorityText(Model model) {
        fillModelWithFormSubmitButtonsTexts(model,
                "Создать роль", "Сохранить");
    }

    private void fillModelWithAnySingleError(Model model, BindingResult bindingResult, String attributeName) {
        model.addAttribute(attributeName, bindingResult.getAllErrors().stream()
                .findAny()
                .orElse(null));
    }

}
