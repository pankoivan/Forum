package org.forum.controllers.mvc;

import jakarta.validation.Valid;
import org.forum.entities.Authority;
import org.forum.entities.Role;
import org.forum.services.interfaces.RoleAuthorityService;
import org.forum.utils.AuthenticationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/roles-authorities")
public class RolesAuthoritiesController {

    private static final String CREATE_ROLE = "Создать роль";

    private static final String CREATE_AUTHORITY = "Создать право";

    private static final String SAVE = "Сохранить";

    private final RoleAuthorityService service;

    @Autowired
    public RolesAuthoritiesController(RoleAuthorityService service) {
        this.service = service;
    }

    @GetMapping
    public String returnRolesAuthoritiesPage(Model model, Authentication authentication) {

        fillModelWithAllAttributes(model, authentication, service.newRole(), service.newAuthority(),
                CREATE_ROLE, CREATE_AUTHORITY);

        return "roles-authorities-panel";
    }

    @PostMapping("/inner/role/create")
    public String redirectRolesAuthoritiesPageAfterCreatingRole(Model model,
                                                                Authentication authentication,
                                                                @Valid Role role,
                                                                BindingResult bindingResult) {

        if (service.roleFullValidation(role, bindingResult)) {

            fillModelWithAllAttributes(model, authentication, role, service.newAuthority(),
                    service.isNewRole(role) ? CREATE_ROLE : SAVE, CREATE_AUTHORITY);

            model.addAttribute("roleError", service.extractAnySingleError(bindingResult));

            return "roles-authorities-panel";
        }

        service.saveRole(role);

        return "redirect:/roles-authorities";
    }

    @PostMapping("/inner/role/edit/{id}")
    public String returnRolesAuthoritiesPageForEditingRole(Model model,
                                                           Authentication authentication,
                                                           @PathVariable("id") Integer id) {

        fillModelWithAllAttributes(model, authentication, service.findRoleById(id), service.newAuthority(),
                SAVE, CREATE_AUTHORITY);

        return "roles-authorities-panel";
    }

    @PostMapping("/inner/role/delete/{id}")
    public String redirectRolesAuthoritiesPageAfterDeletingRole(Model model,
                                                                Authentication authentication,
                                                                @PathVariable("id") Integer id) {

        Role role = service.findRoleById(id);
        if (service.roleDeletingValidation(role)) {

            fillModelWithAllAttributes(model, authentication, service.newRole(), service.newAuthority(),
                    CREATE_ROLE, CREATE_AUTHORITY);

            model.addAttribute("roleError", "Роль \"" + role.getName() + "\" нельзя " +
                    "удалить, так как она назначена каким-то пользователям");

            return "roles-authorities-panel";
        }

        service.deleteRoleById(id);

        return "redirect:/roles-authorities";
    }

    @PostMapping("/inner/authority/create")
    public String redirectRolesAuthoritiesPageAfterCreatingAuthority(Model model,
                                                                     Authentication authentication,
                                                                     @Valid Authority authority,
                                                                     BindingResult bindingResult) {

        if (service.authorityFullValidation(authority, bindingResult)) {

            fillModelWithAllAttributes(model, authentication, service.newRole(), authority,
                    CREATE_ROLE, service.isNewAuthority(authority) ? CREATE_AUTHORITY : SAVE);

            model.addAttribute("authorityError", service.extractAnySingleError(bindingResult));

            return "roles-authorities-panel";
        }

        service.saveAuthority(authority);

        return "redirect:/roles-authorities";
    }

    @PostMapping("/inner/authority/edit/{id}")
    public String returnRolesAuthoritiesPageForEditingAuthority(Model model,
                                                                Authentication authentication,
                                                                @PathVariable("id") Integer id) {

        fillModelWithAllAttributes(model, authentication, service.newRole(), service.findAuthorityById(id),
                CREATE_ROLE, SAVE);

        return "roles-authorities-panel";
    }

    @PostMapping("/inner/authority/delete/{id}")
    public String redirectRolesAuthoritiesPageAfterDeletingAuthority(Model model,
                                                                     Authentication authentication,
                                                                     @PathVariable("id") Integer id) {

        Authority authority = service.findAuthorityById(id);
        if (service.authorityDeletingValidation(authority)) {

            fillModelWithAllAttributes(model, authentication, service.newRole(), service.newAuthority(),
                    CREATE_ROLE, CREATE_AUTHORITY);

            model.addAttribute("authorityError", "Право \"" + authority.getName() + "\" нельзя" +
                    " удалить, так как для каких-то ролей оно является единственным");

            return "roles-authorities-panel";
        }

        service.deleteAuthorityById(id);

        return "redirect:/roles-authorities";
    }

    private void fillModelWithAllAttributes(Model model,
                                            Authentication authentication,
                                            Role role,
                                            Authority authority,
                                            String roleFormSubmitButtonText,
                                            String authorityFormSubmitButtonText) {

        model.addAttribute("currentUser", AuthenticationUtils.extractCurrentUserOrNull(authentication));
        model.addAttribute("roles", service.findAllRoles());
        model.addAttribute("authorities", service.findAllAuthorities());
        model.addAttribute("role", role);
        model.addAttribute("authority", authority);
        model.addAttribute("roleFormSubmitButtonText", roleFormSubmitButtonText);
        model.addAttribute("authorityFormSubmitButtonText", authorityFormSubmitButtonText);
    }

}
