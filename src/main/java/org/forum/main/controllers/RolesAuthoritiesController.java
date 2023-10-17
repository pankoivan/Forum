package org.forum.main.controllers;

import org.forum.auxiliary.constants.url.ControllerBaseUrlConstants;
import org.forum.main.controllers.common.ConvenientController;
import org.forum.main.services.interfaces.AuthorityService;
import org.forum.main.services.interfaces.RoleService;
import org.forum.main.services.interfaces.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(ControllerBaseUrlConstants.FOR_ROLES_AUTHORITIES_CONTROLLER)
public class RolesAuthoritiesController extends ConvenientController {

    private final SectionService sectionService;

    private final RoleService roleService;

    private final AuthorityService authorityService;

    @Autowired
    public RolesAuthoritiesController(SectionService sectionService, RoleService roleService,
                                      AuthorityService authorityService) {
        this.sectionService = sectionService;
        this.roleService = roleService;
        this.authorityService = authorityService;
    }

    @GetMapping
    public String returnRolesAuthoritiesPage(Model model, Authentication authentication) {

        addForHeader(model, authentication, sectionService);
        add(model, "roles", roleService.findAll());
        add(model, "authorities", authorityService.findAll());
        add(model, "role", roleService.empty());
        add(model, "authority", authorityService.empty());
        add(model, "roleFormSubmitButtonText", "Создать роль");
        add(model, "authorityFormSubmitButtonText", "Создать право");

        return "roles-authorities-panel";
    }

}
