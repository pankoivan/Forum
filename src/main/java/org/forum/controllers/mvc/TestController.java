package org.forum.controllers.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {

    @GetMapping("/")
    public String testReturn() {
        System.out.println("Hello Word!");
        return "index";
    }

}
