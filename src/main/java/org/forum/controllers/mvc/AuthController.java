package org.forum.controllers.mvc;

import org.forum.entities.User;
import org.forum.entities.enums.Gender;
import org.forum.utils.AuthenticationUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @GetMapping("/registration")
    public String returnRegistrationPage(Authentication authentication, Model model) {
        model.addAttribute("currentUser", AuthenticationUtils.extractCurrentUserOrNull(authentication));
        model.addAttribute("user", new User());
        model.addAttribute("genders", Gender.values());
        return "registration";
    }

    @PostMapping("/registration/processing")
    public String redirectLoginAfterRegistration(User user) {
        System.out.println(user);
        return "redirect:/auth/login";
    }

    @GetMapping("/login")
    public String returnLoginPage(Authentication authentication, Model model) {
        model.addAttribute("currentUser", AuthenticationUtils.extractCurrentUserOrNull(authentication));
        return "login";
    }

}
