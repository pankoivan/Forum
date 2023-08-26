package org.forum.controllers.mvc;

import jakarta.validation.Valid;
import org.forum.controllers.mvc.interfaces.ConvenientController;
import org.forum.entities.Authority;
import org.forum.services.interfaces.AuthorityService;
import org.forum.services.interfaces.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/roles-authorities/authorities")
public class AuthoritiesController implements ConvenientController {

    private final RoleService roleService;

    private final AuthorityService authorityService;

    @Autowired
    public AuthoritiesController(RoleService roleService, AuthorityService authorityService) {
        this.roleService = roleService;
        this.authorityService = authorityService;
    }

    @PostMapping("/inner/save")
    public String redirectRolesAuthoritiesPageAfterSavingAuthority(Model model,
                                                                   Authentication authentication,
                                                                   @Valid Authority authority,
                                                                   BindingResult bindingResult) {

        if (authorityService.savingValidation(authority, bindingResult)) {
            addCurrentUser(model, authentication);
            add(model, "roles", roleService.findAll());
            add(model, "authorities", authorityService.findAll());
            add(model, "role", roleService.empty());
            add(model, "authority", authority);
            add(model, "roleFormSubmitButtonText", "Создать роль");
            add(model, "authorityFormSubmitButtonText", authorityService.isNew(authority)
                    ? "Создать право" : "Сохранить");
            add(model, "authorityError", authorityService.extractAnySingleError(bindingResult));
            return "roles-authorities-panel";
        }

        authorityService.save(authority);
        return "redirect:/roles-authorities";
    }

    @PostMapping("/inner/edit/{id}")
    public String returnRolesAuthoritiesPageForEditingAuthority(Model model,
                                                                Authentication authentication,
                                                                @PathVariable("id") Integer id) {

        addCurrentUser(model, authentication);
        add(model, "roles", roleService.findAll());
        add(model, "authorities", authorityService.findAll());
        add(model, "role", roleService.empty());
        add(model, "authority", authorityService.findById(id));
        add(model, "roleFormSubmitButtonText", "Создать роль");
        add(model, "authorityFormSubmitButtonText", "Сохранить");
        return "roles-authorities-panel";
    }

    @PostMapping("/inner/delete/{id}")
    public String redirectRolesAuthoritiesPageAfterDeletingAuthority(Model model,
                                                                     Authentication authentication,
                                                                     @PathVariable("id") Integer id) {

        String msg = authorityService.deletingValidation(authorityService.findById(id));
        if (msg != null) {
            addCurrentUser(model, authentication);
            add(model, "roles", roleService.findAll());
            add(model, "authorities", authorityService.findAll());
            add(model, "role", roleService.empty());
            add(model, "authority", authorityService.empty());
            add(model, "roleFormSubmitButtonText", "Создать роль");
            add(model, "authorityFormSubmitButtonText", "Создать право");
            add(model, "authorityError", msg);
            return "roles-authorities-panel";
        }

        authorityService.deleteById(id);
        return "redirect:/roles-authorities";
    }

}
