package org.forum.controllers.mvc;

import org.forum.entities.Authority;
import org.forum.entities.Role;
import org.forum.services.implementations.RoleAuthorityServiceImpl;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/roles-authorities")
public class RolesAuthoritiesController {

    private final RoleAuthorityServiceImpl roleAuthorityService;

    public RolesAuthoritiesController(RoleAuthorityServiceImpl roleAuthorityService) {
        this.roleAuthorityService = roleAuthorityService;
    }

    @GetMapping
    public String returnRolesAuthoritiesPanel(Model model, Authentication authentication) {
        roleAuthorityService.fillRolesAuthoritiesPage(model, authentication);
        return "roles-authorities-panel";
    }

    @PostMapping("/inner/authority/create")
    public String redirectRolesAuthoritiesAfterCreationAuthority(Authority authority) {
        roleAuthorityService.saveAuthority(authority);
        return "redirect:/roles-authorities";
    }

    @PostMapping("/inner/authority/edit/{id}")
    public String redirectRolesAuthoritiesAfterEditingAuthority(Model model,
                                                         Authentication authentication,
                                                         @PathVariable("id") Integer id) {

        roleAuthorityService.fillRolesAuthoritiesPageForAuthorityEditing(model, authentication, id);
        return "redirect:/roles-authorities";
    }

    @PostMapping("/inner/authority/delete/{id}")
    public String redirectRolesAuthoritiesAfterDeletingAuthority(Model model,
                                                         Authentication authentication,
                                                         @PathVariable("id") Integer id) {

        roleAuthorityService.deleteAuthorityById(id);
        return "redirect:/roles-authorities";
    }

    @PostMapping("/inner/role/create")
    public String redirectRolesAuthoritiesAfterCreationRole(Role role,
                                                         @RequestParam("selectedAuthorities")
                                                         List<Authority> authorities) {

        roleAuthorityService.saveRole(role, authorities);
        return "redirect:/roles-authorities";
    }

    @PostMapping("/inner/role/edit/{id}")
    public String redirectRolesAuthoritiesAfterEditingRole(Model model,
                                                         Authentication authentication,
                                                         @PathVariable("id") Integer id) {

        roleAuthorityService.fillRolesAuthoritiesPageForRoleEditing(model, authentication, id);
        return "redirect:/roles-authorities";
    }

    @PostMapping("/inner/role/delete/{id}")
    public String redirectRolesAuthoritiesAfterDeletingRole(Model model,
                                                         Authentication authentication,
                                                         @PathVariable("id") Integer id) {

        roleAuthorityService.deleteRoleById(id);
        return "redirect:/roles-authorities";
    }

}
