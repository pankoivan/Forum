package org.forum.main.controllers.common;

import jakarta.servlet.http.HttpSession;
import org.forum.auxiliary.constants.CommonAttributeNameConstants;
import org.forum.auxiliary.constants.sorting.SortingAttributeNameConstants;
import org.forum.auxiliary.constants.url.ControllerBaseUrlConstants;
import org.forum.auxiliary.constants.url.UrlPartConstants;
import org.forum.auxiliary.sorting.options.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(ControllerBaseUrlConstants.FOR_SORTING_CONTROLLER)
@PreAuthorize("permitAll()")
public class SortingController {

    @PostMapping("/" + UrlPartConstants.USERS)
    public String redirectSourcePageAfterSortingUsers(HttpSession session,
                                                      UserSortingOption sortingOption,
                                                      @RequestParam(SortingAttributeNameConstants.SORTING_OPTION_NAME)
                                                          String sortingOptionName,
                                                      @RequestParam(CommonAttributeNameConstants.SOURCE_PAGE_URL_WITHOUT_PAGINATION)
                                                          String sortingSourcePageUrl) {

        session.setAttribute(sortingOptionName, sortingOption);
        return "redirect:%s".formatted(sortingSourcePageUrl);
    }

    @PostMapping("/" + UrlPartConstants.SECTIONS)
    public String redirectSourcePageAfterSortingSections(HttpSession session,
                                                         SectionSortingOption sortingOption,
                                                         @RequestParam(SortingAttributeNameConstants.SORTING_OPTION_NAME)
                                                             String sortingOptionName,
                                                         @RequestParam(CommonAttributeNameConstants.SOURCE_PAGE_URL_WITHOUT_PAGINATION)
                                                             String sortingSourcePageUrl) {

        session.setAttribute(sortingOptionName, sortingOption);
        return "redirect:%s".formatted(sortingSourcePageUrl);
    }

    @PostMapping("/" + UrlPartConstants.TOPICS)
    public String redirectSourcePageAfterSortingTopics(HttpSession session,
                                                       TopicSortingOption sortingOption,
                                                       @RequestParam(SortingAttributeNameConstants.SORTING_OPTION_NAME)
                                                           String sortingOptionName,
                                                       @RequestParam(CommonAttributeNameConstants.SOURCE_PAGE_URL_WITHOUT_PAGINATION)
                                                           String sortingSourcePageUrl) {

        session.setAttribute(sortingOptionName, sortingOption);
        return "redirect:%s".formatted(sortingSourcePageUrl);
    }

    @PostMapping("/" + UrlPartConstants.MESSAGES)
    public String redirectSourcePageAfterSortingMessages(HttpSession session,
                                                         MessageSortingOption sortingOption,
                                                         @RequestParam(SortingAttributeNameConstants.SORTING_OPTION_NAME)
                                                             String sortingOptionName,
                                                         @RequestParam(CommonAttributeNameConstants.SOURCE_PAGE_URL_WITHOUT_PAGINATION)
                                                             String sortingSourcePageUrl) {

        session.setAttribute(sortingOptionName, sortingOption);
        return "redirect:%s".formatted(sortingSourcePageUrl);
    }

    @PostMapping("/" + UrlPartConstants.BANS)
    public String redirectSourcePageAfterSortingBans(HttpSession session,
                                                     BanSortingOption sortingOption,
                                                        @RequestParam(SortingAttributeNameConstants.SORTING_OPTION_NAME)
                                                     String sortingOptionName,
                                                        @RequestParam(CommonAttributeNameConstants.SOURCE_PAGE_URL_WITHOUT_PAGINATION)
                                                     String sortingSourcePageUrl) {

        session.setAttribute(sortingOptionName, sortingOption);
        return "redirect:%s".formatted(sortingSourcePageUrl);
    }

}
