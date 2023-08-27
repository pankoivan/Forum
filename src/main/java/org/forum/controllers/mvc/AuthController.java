package org.forum.controllers.mvc;

import org.forum.controllers.mvc.common.ConvenientController;
import org.forum.services.interfaces.SectionService;
import org.forum.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthController extends ConvenientController {

    private final SectionService sectionService;

    private final UserService service;

    @Autowired
    public AuthController(SectionService sectionService, UserService service) {
        this.sectionService = sectionService;
        this.service = service;
    }

    @GetMapping("/login")
    public String returnLoginPage(Model model, Authentication authentication) {
        addForHeader(model, authentication, sectionService);
        return "login";
    }

    @GetMapping("/registration")
    public String returnRegistrationPage(Model model, Authentication authentication) {
        addForHeader(model, authentication, sectionService);
        model.addAttribute("user", service.empty());
        model.addAttribute("genders", service.genders());
        return "registration";
    }

    @PostMapping("/registration/processing")
    public String redirectLoginPageAfterRegistration() {
        return "redirect:/auth/login";
    }

}
