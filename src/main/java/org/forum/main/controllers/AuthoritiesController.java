package org.forum.main.controllers;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.forum.auxiliary.constants.url.ControllerBaseUrlConstants;
import org.forum.main.controllers.common.ConvenientController;
import org.forum.main.entities.Authority;
import org.forum.main.services.interfaces.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(ControllerBaseUrlConstants.FOR_AUTHORITIES_CONTROLLER)
@PreAuthorize("hasRole('OWNER')")
public class AuthoritiesController extends ConvenientController {

    private final AuthorityService service;

    @Autowired
    public AuthoritiesController(AuthorityService service) {
        this.service = service;
    }

    @PostMapping("/save")
    public String redirectRolesAuthoritiesPageAfterSavingAuthority(HttpSession session,
                                                                   @Valid Authority authority,
                                                                   BindingResult bindingResult) {

        if (service.savingValidation(authority, bindingResult)) {
            session.setAttribute("authority", authority);
            session.setAttribute("authorityFormSubmitButtonText", service.isNew(authority) ? "Создать право" : "Сохранить");
            session.setAttribute("authorityErrorMessage", service.anyError(bindingResult));
            return "redirect:%s".formatted(ControllerBaseUrlConstants.FOR_ROLES_AUTHORITIES_CONTROLLER);
        }

        service.save(authority);
        return "redirect:%s".formatted(ControllerBaseUrlConstants.FOR_ROLES_AUTHORITIES_CONTROLLER);
    }

    @PostMapping("/edit/{id}")
    public String returnRolesAuthoritiesPageForEditingAuthority(HttpSession session, @PathVariable("id") String pathId) {

        Integer id = toNonNegativeInteger(pathId);

        session.setAttribute("authority", service.findById(id));
        session.setAttribute("authorityFormSubmitButtonText", "Сохранить");
        return "redirect:%s".formatted(ControllerBaseUrlConstants.FOR_ROLES_AUTHORITIES_CONTROLLER);
    }

    @PostMapping("/delete/{id}")
    public String redirectRolesAuthoritiesPageAfterDeletingAuthority(HttpSession session, @PathVariable("id") String pathId) {

        Integer id = toNonNegativeInteger(pathId);

        String msg = service.deletingValidation(service.findById(id));
        if (msg != null) {
            session.setAttribute("authorityErrorMessage", msg);
            return "redirect:%s".formatted(ControllerBaseUrlConstants.FOR_ROLES_AUTHORITIES_CONTROLLER);
        }

        service.deleteById(id);
        return "redirect:%s".formatted(ControllerBaseUrlConstants.FOR_ROLES_AUTHORITIES_CONTROLLER);
    }

}
