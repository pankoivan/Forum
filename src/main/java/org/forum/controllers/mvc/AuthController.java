package org.forum.controllers.mvc;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/auth")
public class AuthController {

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

    @GetMapping("/registration")
    public String returnRegistrationPage() {
        return "registration";
    }

}
