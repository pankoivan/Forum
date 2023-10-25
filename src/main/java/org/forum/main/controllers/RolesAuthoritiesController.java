package org.forum.main.controllers;

import jakarta.servlet.http.HttpSession;
import org.forum.auxiliary.constants.url.ControllerBaseUrlConstants;
import org.forum.main.controllers.common.ConvenientController;
import org.forum.main.entities.Authority;
import org.forum.main.entities.Role;
import org.forum.main.services.interfaces.AuthorityService;
import org.forum.main.services.interfaces.RoleService;
import org.forum.main.services.interfaces.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
@RequestMapping(ControllerBaseUrlConstants.FOR_ROLES_AUTHORITIES_CONTROLLER)
@PreAuthorize("hasRole('OWNER')")
public class RolesAuthoritiesController extends ConvenientController {

    private final SectionService sectionService;

    private final RoleService roleService;

    private final AuthorityService authorityService;

    @Autowired
    public RolesAuthoritiesController(SectionService sectionService, RoleService roleService, AuthorityService authorityService) {
        this.sectionService = sectionService;
        this.roleService = roleService;
        this.authorityService = authorityService;
    }

    @GetMapping
    public String returnRolesAuthoritiesPage(HttpSession session,
                                             Model model,
                                             Authentication authentication,
                                             @SessionAttribute(value = "role", required = false) Role role,
                                             @SessionAttribute(value = "roleFormSubmitButtonText", required = false)
                                                 String roleFormSubmitButtonText,
                                             @SessionAttribute(value = "roleErrorMessage", required = false)
                                                 String roleErrorMessage,
                                             @SessionAttribute(value = "authority", required = false) Authority authority,
                                             @SessionAttribute(value = "authorityFormSubmitButtonText", required = false)
                                                 String authorityFormSubmitButtonText,
                                             @SessionAttribute(value = "authorityErrorMessage", required = false)
                                                 String authorityErrorMessage) {

        addForHeader(model, authentication, sectionService);
        add(model, "roles", roleService.findAll());
        add(model, "authorities", authorityService.findAll());
        add(model, "role", roleService.empty());
        add(model, "authority", authorityService.empty());
        add(model, "roleFormSubmitButtonText", "Создать роль");
        add(model, "authorityFormSubmitButtonText", "Создать право");

        if (roleErrorMessage != null) {
            add(model, "roleError", roleErrorMessage);
            session.removeAttribute("roleErrorMessage");
        }
        if (role != null) {
            add(model, "role", role);
            add(model, "roleFormSubmitButtonText", roleFormSubmitButtonText);
            session.removeAttribute("role");
            session.removeAttribute("roleFormSubmitButtonText");
        }
        if (authorityErrorMessage != null) {
            add(model, "authorityError", authorityErrorMessage);
            session.removeAttribute("authorityErrorMessage");
        }
        if (authority != null) {
            add(model, "authority", authority);
            add(model, "authorityFormSubmitButtonText", authorityFormSubmitButtonText);
            session.removeAttribute("authority");
            session.removeAttribute("authorityFormSubmitButtonText");
        }

        return "roles-authorities-panel";
    }

}
