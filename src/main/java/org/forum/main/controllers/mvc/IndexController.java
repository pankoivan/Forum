package org.forum.main.controllers.mvc;

import org.forum.main.controllers.mvc.common.ConvenientController;
import org.forum.main.services.interfaces.StatisticsService;
import org.forum.main.services.interfaces.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class IndexController extends ConvenientController {

    private final SectionService sectionService;

    private final StatisticsService service;

    @Autowired
    public IndexController(SectionService sectionService, StatisticsService service) {
        this.sectionService = sectionService;
        this.service = service;
    }

    @GetMapping
    public String returnIndexPage(Authentication authentication, Model model) {
        addForHeader(model, authentication, sectionService);
        add(model, "page", "index");
        add(model, "topUsers", service.topUsers());
        add(model, "recentMessages", service.recentMessages());
        statistics(model);
        return "index";
    }

    private void statistics(Model model) {
        add(model, "usersCount", service.usersCount());
        add(model, "usualUsersCount", service.usualUsersCount());
        add(model, "adminsCount", service.adminsCount());
        add(model, "modersCount", service.modersCount());
        add(model, "messagesCount", service.messagesCount());
        add(model, "likesCount", service.likesCount());
        add(model, "sectionsCount", service.sectionsCount());
        add(model, "topicsCount", service.topicsCount());
        add(model, "bansCount", service.bansCount());
        add(model, "currentBansCount", service.currentBansCount());
    }

}
