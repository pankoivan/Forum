package org.forum.main.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.forum.auxiliary.constants.CommonAttributeNameConstants;
import org.forum.auxiliary.constants.url.ControllerBaseUrlConstants;
import org.forum.main.controllers.common.ConvenientController;
import org.forum.main.services.interfaces.StatisticsService;
import org.forum.main.services.interfaces.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(ControllerBaseUrlConstants.FOR_INDEX_CONTROLLER)
@PreAuthorize("permitAll()")
public class IndexController extends ConvenientController {

    private final SectionService sectionService;

    private final StatisticsService service;

    @Autowired
    public IndexController(SectionService sectionService, StatisticsService service) {
        this.sectionService = sectionService;
        this.service = service;
    }

    @GetMapping
    public String returnIndexPage(HttpServletRequest request,
                                  Model model,
                                  Authentication authentication) {

        addForHeader(model, authentication, sectionService);
        add(model, "topUsers", service.topUsers());
        add(model, "recentMessages", service.recentMessages());
        add(model, "usersCount", service.usersCount());
        add(model, "usualUsersCount", service.usualUsersCount());
        add(model, "modersCount", service.modersCount());
        add(model, "adminsCount", service.adminsCount());
        add(model, "sectionsCount", service.sectionsCount());
        add(model, "topicsCount", service.topicsCount());
        add(model, "messagesCount", service.messagesCount());
        add(model, "likesCount", service.likesCount());
        add(model, "dislikesCount", service.dislikesCount());
        add(model, "bansCount", service.bansCount());
        add(model, "currentBansCount", service.currentBansCount());
        add(model, CommonAttributeNameConstants.PAGE, "index");
        add(model, CommonAttributeNameConstants.IS_EDIT_DELETE_BUTTONS_ENABLED, false);
        add(model, CommonAttributeNameConstants.IS_LIKE_DISLIKE_BUTTONS_ENABLED, true);
        add(model, CommonAttributeNameConstants.SOURCE_PAGE_URL_WITH_PAGINATION, request.getRequestURI());
        add(model, CommonAttributeNameConstants.SOURCE_PAGE_URL_WITHOUT_PAGINATION, removePagination(request.getRequestURI()));

        return "index";
    }

}
