package org.forum.main.controllers.mvc;

import org.forum.main.controllers.mvc.common.ConvenientController;
import org.forum.main.services.interfaces.SectionService;
import org.forum.main.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UsersController extends ConvenientController {

    private final SectionService sectionService;

    private final UserService service;

    @Autowired
    public UsersController(SectionService sectionService, UserService service) {
        this.sectionService = sectionService;
        this.service = service;
    }

    @GetMapping
    public String returnUsersPageWithPagination() {
        return "redirect:/users/page1";
    }

    @GetMapping("/page{pageNumber}")
    public String returnUsersPage(Model model,
                                  Authentication authentication,
                                  @PathVariable("pageNumber") Integer pageNumber) {

        addForHeader(model, authentication, sectionService);
        add(model, "users", service.onPage(service.findAll(), pageNumber));
        add(model, "pagesCount", service.pagesCount(service.findAll()));
        add(model, "currentPage", pageNumber);
        return "users";
    }

}
