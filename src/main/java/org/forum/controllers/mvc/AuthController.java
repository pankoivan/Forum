package org.forum.controllers.mvc;

import org.forum.entities.User;
import org.forum.entities.enums.Gender;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @GetMapping("/registration")
    public String returnRegistrationPage(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("genders", Gender.values());
        return "registration";
    }

    @PostMapping("/registration-processing")
    public String redirectLoginAfterRegistration(User user) {
        System.out.println(user);
        return "redirect:login";
    }

    @GetMapping("/login")
    public String returnLoginPage(Authentication authentication, Principal principal) {
        /*System.out.println();
        System.out.println(authentication);
        System.out.println(principal);
        System.out.println();
        System.out.println(SecurityContextHolder.getContext().getAuthentication());
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal());*/
        return "login";
    }

}
