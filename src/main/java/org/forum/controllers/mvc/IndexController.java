package org.forum.controllers.mvc;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/")
public class IndexController {

    @GetMapping
    public String returnIndexPage(Authentication authentication, Principal principal) {
        System.out.println();
        System.out.println(authentication);
        System.out.println(principal);
        System.out.println();
        System.out.println(SecurityContextHolder.getContext().getAuthentication());
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        System.out.println();
        System.out.println();
        return "index";
    }

}
