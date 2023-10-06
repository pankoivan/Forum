package org.forum.main.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users/{id}/contributions")
public class UsersContributionsController {

    @GetMapping("/liked")
    public String method1() {
        return null;
    }

    @GetMapping("/disliked")
    public String method2() {
        return null;
    }

    @GetMapping("/messages")
    public String method3() {
        return null;
    }

    @GetMapping("/topics")
    public String method4() {
        return null;
    }

    @GetMapping("/sections")
    public String method5() {
        return null;
    }

}
