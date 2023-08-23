package org.forum.controllers.mvc;

import org.forum.repositories.AuthorityRepository;
import org.forum.repositories.RoleRepository;
import org.forum.utils.AuthenticationUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/roles-authorities")
public class RolesAuthoritiesController {

    private final RoleRepository roleRepository;

    private final AuthorityRepository authorityRepository;

    public RolesAuthoritiesController(RoleRepository roleRepository, AuthorityRepository authorityRepository) {
        this.roleRepository = roleRepository;
        this.authorityRepository = authorityRepository;
    }

    @GetMapping
    public String returnRolesAuthoritiesPanel(Authentication authentication, Model model) {
        model.addAttribute("currentUser", AuthenticationUtils.extractCurrentUserOrNull(authentication));
        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("authorities", authorityRepository.findAll());
        return "roles-authorities-panel";
    }

}
