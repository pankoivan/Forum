package org.forum.controllers.mvc;

import org.forum.controllers.mvc.common.ConvenientController;
import org.forum.services.interfaces.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class IndexController extends ConvenientController {

    private final SectionService service;

    @Autowired
    public IndexController(SectionService service) {
        this.service = service;
    }

    @GetMapping
    public String returnIndexPage(Authentication authentication, Model model) {
        addForHeader(model, authentication, service);
        add(model, "page", "index");
        return "index";
    }

}
