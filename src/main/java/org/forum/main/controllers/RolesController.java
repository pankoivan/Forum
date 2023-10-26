package org.forum.main.controllers;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.forum.auxiliary.constants.url.ControllerBaseUrlConstants;
import org.forum.main.controllers.common.ConvenientController;
import org.forum.main.entities.Role;
import org.forum.main.services.interfaces.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(ControllerBaseUrlConstants.FOR_ROLES_CONTROLLER)
@PreAuthorize("hasRole('OWNER')")
public class RolesController extends ConvenientController {

    private final RoleService service;

    @Autowired
    public RolesController(RoleService service) {
        this.service = service;
    }

    @PostMapping("/save")
    public String redirectRolesAuthoritiesPageAfterSavingRole(HttpSession session,
                                                              @Valid Role role,
                                                              BindingResult bindingResult) {

        if (service.savingValidation(role, bindingResult)) {
            session.setAttribute("role", role);
            session.setAttribute("roleFormSubmitButtonText", service.isNew(role) ? "Создать роль" : "Сохранить");
            session.setAttribute("roleErrorMessage", service.anyError(bindingResult));
            return "redirect:%s".formatted(ControllerBaseUrlConstants.FOR_ROLES_AUTHORITIES_CONTROLLER);
        }

        service.save(role);
        return "redirect:%s".formatted(ControllerBaseUrlConstants.FOR_ROLES_AUTHORITIES_CONTROLLER);
    }

    @PostMapping("/edit/{id}")
    public String returnRolesAuthoritiesPageForEditingRole(HttpSession session, @PathVariable("id") String pathId) {

        Integer id = toNonNegativeInteger(pathId);

        session.setAttribute("role", service.findById(id));
        session.setAttribute("roleFormSubmitButtonText", "Сохранить");

        return "redirect:%s".formatted(ControllerBaseUrlConstants.FOR_ROLES_AUTHORITIES_CONTROLLER);
    }

    @PostMapping("/delete/{id}")
    public String redirectRolesAuthoritiesPageAfterDeletingRole(HttpSession session, @PathVariable("id") String pathId) {

        Integer id = toNonNegativeInteger(pathId);

        String msg = service.deletingValidation(service.findById(id));
        if (msg != null) {
            session.setAttribute("roleErrorMessage", msg);
            return "redirect:%s".formatted(ControllerBaseUrlConstants.FOR_ROLES_AUTHORITIES_CONTROLLER);
        }

        service.deleteById(id);
        return "redirect:%s".formatted(ControllerBaseUrlConstants.FOR_ROLES_AUTHORITIES_CONTROLLER);
    }

}
