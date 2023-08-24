package org.forum.controllers.mvc;

import jakarta.validation.Valid;
import org.forum.entities.Authority;
import org.forum.entities.Role;
import org.forum.services.interfaces.RoleAuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/roles-authorities")
public class RolesAuthoritiesController {

    private final RoleAuthorityService roleAuthorityService;

    @Autowired
    public RolesAuthoritiesController(RoleAuthorityService roleAuthorityService) {
        this.roleAuthorityService = roleAuthorityService;
    }

    @GetMapping
    public String returnRolesAuthorities(Model model, Authentication authentication) {
        roleAuthorityService.fillModel(model, authentication);
        return "roles-authorities-panel";
    }

    @PostMapping("/inner/role/create")
    public String redirectRolesAuthoritiesPageAfterCreatingRole(Model model,
                                                                Authentication authentication,
                                                                @Valid Role role,
                                                                BindingResult bindingResult,
                                                                @RequestParam(value = "selectedAuthorities",
                                                                        required = false)
                                                                List<Authority> authorities) {
        if (bindingResult.hasErrors()) {
            roleAuthorityService.fillModelForRoleErrors(model, authentication, role, authorities, bindingResult);
            return "roles-authorities-panel";
        }

        roleAuthorityService.saveRole(role, authorities);
        return "redirect:/roles-authorities";
    }

    @PostMapping("/inner/role/edit/{id}")
    public String redirectRolesAuthoritiesPageAfterEditingRole(Model model,
                                                               Authentication authentication,
                                                               @PathVariable("id") Integer id) {
        roleAuthorityService
                .fillModelForRoleEditing(model, authentication, id);
        return "roles-authorities-panel";
    }

    @PostMapping("/inner/role/delete/{id}")
    public String redirectRolesAuthoritiesPageAfterDeletingRole(Model model,
                                                                Authentication authentication,
                                                                @PathVariable("id") Integer id) {
        roleAuthorityService.deleteRoleById(id);
        return "redirect:/roles-authorities";
    }

    @PostMapping("/inner/authority/create")
    public String redirectRolesAuthoritiesPageAfterCreatingAuthority(Model model,
                                                                     Authentication authentication,
                                                                     @Valid Authority authority,
                                                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            roleAuthorityService.fillModelForAuthorityErrors(model, authentication, authority, bindingResult);
            return "roles-authorities-panel";
        }

        roleAuthorityService.saveAuthority(authority);
        return "redirect:/roles-authorities";
    }

    @PostMapping("/inner/authority/edit/{id}")
    public String redirectRolesAuthoritiesPageAfterEditingAuthority(Model model,
                                                         Authentication authentication,
                                                         @PathVariable("id") Integer id) {
        roleAuthorityService
                .fillModelForAuthorityEditing(model, authentication, id);
        return "roles-authorities-panel";
    }

    @PostMapping("/inner/authority/delete/{id}")
    public String redirectRolesAuthoritiesPageAfterDeletingAuthority(Model model,
                                                         Authentication authentication,
                                                         @PathVariable("id") Integer id) {
        roleAuthorityService.deleteAuthorityById(id);
        return "redirect:/roles-authorities";
    }

}
