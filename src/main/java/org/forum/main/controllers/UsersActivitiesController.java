package org.forum.main.controllers;

import org.forum.main.controllers.common.ConvenientController;
import org.forum.main.services.interfaces.RoleService;
import org.forum.main.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users/activities")
public class UsersActivitiesController extends ConvenientController {

    private final UserService service;

    private final RoleService roleService;

    @Autowired
    public UsersActivitiesController(UserService service, RoleService roleService) {
        this.service = service;
        this.roleService = roleService;
    }

    @PostMapping("/assign-moder/{id}")
    public String redirectUserProfilePageAfterAssigningModeration(@PathVariable("id") String pathId) {
        Integer id = toNonNegativeInteger(pathId);
        service.changeRole(service.findById(id), roleService.findByName("ROLE_MODER"));
        return "redirect:/users/{id}";
    }

    @PostMapping("/assign-admin/{id}")
    public String redirectUserProfilePageAfterAssigningAdministration(@PathVariable("id") String pathId) {
        Integer id = toNonNegativeInteger(pathId);
        service.changeRole(service.findById(id), roleService.findByName("ROLE_ADMIN"));
        return "redirect:/users/{id}";
    }

    @PostMapping("/assign-user/{id}")
    public String redirectUserProfilePageAfterRemovingModerationOrAdministration(@PathVariable("id") String pathId) {
        Integer id = toNonNegativeInteger(pathId);
        service.changeRole(service.findById(id), roleService.findByName("ROLE_USER"));
        return "redirect:/users/{id}";
    }

}
