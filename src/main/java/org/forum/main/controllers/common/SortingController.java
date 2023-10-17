package org.forum.main.controllers.common;

import jakarta.servlet.http.HttpSession;
import org.forum.auxiliary.constants.url.ControllerBaseUrlConstants;
import org.forum.auxiliary.constants.url.UrlPartConstants;
import org.forum.auxiliary.sorting.options.MessageSortingOption;
import org.forum.auxiliary.sorting.options.SectionSortingOption;
import org.forum.auxiliary.sorting.options.TopicSortingOption;
import org.forum.auxiliary.sorting.options.UserSortingOption;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(ControllerBaseUrlConstants.FOR_SORTING_CONTROLLER)
public class SortingController {

    @PostMapping("/" + UrlPartConstants.USERS)
    public String redirectSourcePageAfterSortingUsers(HttpSession session,
                                                      UserSortingOption sortingOption,
                                                      @RequestParam("sortingOptionName") String sortingOptionName,
                                                      @RequestParam("sortingSourcePageUrl") String sortingSourcePageUrl) {

        session.setAttribute(sortingOptionName, sortingOption);
        return "redirect:%s"
                .formatted(sortingSourcePageUrl);
    }

    @PostMapping("/" + UrlPartConstants.SECTIONS)
    public String redirectSourcePageAfterSortingSections(HttpSession session,
                                                         SectionSortingOption sortingOption,
                                                         @RequestParam("sortingOptionName") String sortingOptionName,
                                                         @RequestParam("sortingSourcePageUrl") String sortingSourcePageUrl) {

        session.setAttribute(sortingOptionName, sortingOption);
        return "redirect:%s"
                .formatted(sortingSourcePageUrl);
    }

    @PostMapping("/" + UrlPartConstants.TOPICS)
    public String redirectSourcePageAfterSortingTopics(HttpSession session,
                                                       TopicSortingOption sortingOption,
                                                       @RequestParam("sortingOptionName") String sortingOptionName,
                                                       @RequestParam("sortingSourcePageUrl") String sortingSourcePageUrl) {

        session.setAttribute(sortingOptionName, sortingOption);
        return "redirect:%s"
                .formatted(sortingSourcePageUrl);
    }

    @PostMapping("/" + UrlPartConstants.MESSAGES)
    public String redirectSourcePageAfterSortingMessages(HttpSession session,
                                                         MessageSortingOption sortingOption,
                                                         @RequestParam("sortingOptionName") String sortingOptionName,
                                                         @RequestParam("sortingSourcePageUrl") String sortingSourcePageUrl) {

        session.setAttribute(sortingOptionName, sortingOption);
        return "redirect:%s"
                .formatted(sortingSourcePageUrl);
    }

}
