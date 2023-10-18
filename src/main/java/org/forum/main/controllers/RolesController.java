package org.forum.main.controllers;

import jakarta.validation.Valid;
import org.forum.auxiliary.constants.url.ControllerBaseUrlConstants;
import org.forum.main.controllers.common.ConvenientController;
import org.forum.main.entities.Role;
import org.forum.main.services.interfaces.AuthorityService;
import org.forum.main.services.interfaces.RoleService;
import org.forum.main.services.interfaces.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(ControllerBaseUrlConstants.FOR_ROLES_CONTROLLER)
public class RolesController extends ConvenientController {

    private final SectionService sectionService;

    private final RoleService roleService;

    private final AuthorityService authorityService;

    @Autowired
    public RolesController(SectionService sectionService, RoleService roleService, AuthorityService authorityService) {
        this.sectionService = sectionService;
        this.roleService = roleService;
        this.authorityService = authorityService;
    }

    @PostMapping("/inner/save")
    public String redirectRolesAuthoritiesPageAfterSavingRole(Model model,
                                                              Authentication authentication,
                                                              @Valid Role role,
                                                              BindingResult bindingResult) {

        if (roleService.savingValidation(role, bindingResult)) {

            addForHeader(model, authentication, sectionService);
            add(model, "roles", roleService.findAll());
            add(model, "authorities", authorityService.findAll());
            add(model, "role", role);
            add(model, "authority", authorityService.empty());
            add(model, "roleFormSubmitButtonText", roleService.isNew(role)
                    ? "Создать роль" : "Сохранить");
            add(model, "authorityFormSubmitButtonText", "Создать право");
            add(model, "roleError", roleService.anyError(bindingResult));

            return "roles-authorities-panel";
        }

        roleService.save(role);

        return "redirect:%s"
                .formatted(ControllerBaseUrlConstants.FOR_ROLES_AUTHORITIES_CONTROLLER);
    }

    @PostMapping("/inner/edit/{id}")
    public String returnRolesAuthoritiesPageForEditingRole(Model model,
                                                           Authentication authentication,
                                                           @PathVariable("id") String pathId) {

        Integer id = toNonNegativeInteger(pathId);

        addForHeader(model, authentication, sectionService);
        add(model, "roles", roleService.findAll());
        add(model, "authorities", authorityService.findAll());
        add(model, "role", roleService.findById(id));
        add(model, "authority", authorityService.empty());
        add(model, "roleFormSubmitButtonText", "Сохранить");
        add(model, "authorityFormSubmitButtonText", "Создать право");

        return "roles-authorities-panel";
    }

    @PostMapping("/inner/delete/{id}")
    public String redirectRolesAuthoritiesPageAfterDeletingRole(Model model,
                                                                Authentication authentication,
                                                                @PathVariable("id") String pathId) {

        Integer id = toNonNegativeInteger(pathId);

        String msg = roleService.deletingValidation(roleService.findById(id));
        if (msg != null) {

            addForHeader(model, authentication, sectionService);
            add(model, "roles", roleService.findAll());
            add(model, "authorities", authorityService.findAll());
            add(model, "role", roleService.empty());
            add(model, "authority", authorityService.empty());
            add(model, "roleFormSubmitButtonText", "Создать роль");
            add(model, "authorityFormSubmitButtonText", "Создать право");
            add(model, "roleError", msg);

            return "roles-authorities-panel";
        }

        roleService.deleteById(id);

        return "redirect:%s"
                .formatted(ControllerBaseUrlConstants.FOR_ROLES_AUTHORITIES_CONTROLLER);
    }

}
