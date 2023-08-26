package org.forum.controllers.mvc;

import org.forum.services.interfaces.UserService;
import org.forum.utils.AuthenticationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final UserService service;

    @Autowired
    public AuthController(UserService service) {
        this.service = service;
    }

    @GetMapping("/login")
    public String returnLoginPage(Model model, Authentication authentication) {
        fillModelWithCurrentUserAttribute(model, authentication);
        return "login";
    }

    @GetMapping("/registration")
    public String returnRegistrationPage(Model model, Authentication authentication) {
        fillModelWithCurrentUserAttribute(model, authentication);
        model.addAttribute("user", service.newUser());
        model.addAttribute("genders", service.genders());
        return "registration";
    }

    @PostMapping("/registration/processing")
    public String redirectLoginPageAfterRegistration() {
        return "redirect:/auth/login";
    }

    private void fillModelWithCurrentUserAttribute(Model model, Authentication authentication) {
        model.addAttribute("currentUser", AuthenticationUtils.extractCurrentUserOrNull(authentication));
    }

}
