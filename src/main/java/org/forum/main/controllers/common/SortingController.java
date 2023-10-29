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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(ControllerBaseUrlConstants.FOR_SORTING_CONTROLLER)
@PreAuthorize("permitAll()")
public class SortingController {

    @PostMapping("/" + UrlPartConstants.USERS)
    public String redirectSourcePageAfterSortingUsers(HttpSession session,
                                                      RedirectAttributes redirectAttributes,
                                                      UserSortingOption sortingOption,
                                                      @RequestParam(SortingAttributeNameConstants.SORTING_OPTION_NAME)
                                                          String sortingOptionName,
                                                      @RequestParam(CommonAttributeNameConstants.SOURCE_PAGE_URL_WITHOUT_PAGINATION)
                                                          String sortingSourcePageUrl,
                                                      @RequestParam(value = CommonAttributeNameConstants.SEARCH, required = false)
                                                          String searchedText) {

        session.setAttribute(sortingOptionName, sortingOption);
        if (searchedText != null && !searchedText.isEmpty() && !searchedText.equals("null")) {
            redirectAttributes.addAttribute(CommonAttributeNameConstants.SEARCH, searchedText);
        }
        return "redirect:%s".formatted(sortingSourcePageUrl);
    }

    @PostMapping("/" + UrlPartConstants.SECTIONS)
    public String redirectSourcePageAfterSortingSections(HttpSession session,
                                                         RedirectAttributes redirectAttributes,
                                                         SectionSortingOption sortingOption,
                                                         @RequestParam(SortingAttributeNameConstants.SORTING_OPTION_NAME)
                                                             String sortingOptionName,
                                                         @RequestParam(CommonAttributeNameConstants.SOURCE_PAGE_URL_WITHOUT_PAGINATION)
                                                             String sortingSourcePageUrl,
                                                         @RequestParam(value = CommonAttributeNameConstants.SEARCH, required = false)
                                                             String searchedText) {

        session.setAttribute(sortingOptionName, sortingOption);
        if (searchedText != null && !searchedText.isEmpty() && !searchedText.equals("null")) {
            redirectAttributes.addAttribute(CommonAttributeNameConstants.SEARCH, searchedText);
        }
        return "redirect:%s".formatted(sortingSourcePageUrl);
    }

    @PostMapping("/" + UrlPartConstants.TOPICS)
    public String redirectSourcePageAfterSortingTopics(HttpSession session,
                                                       RedirectAttributes redirectAttributes,
                                                       TopicSortingOption sortingOption,
                                                       @RequestParam(SortingAttributeNameConstants.SORTING_OPTION_NAME)
                                                           String sortingOptionName,
                                                       @RequestParam(CommonAttributeNameConstants.SOURCE_PAGE_URL_WITHOUT_PAGINATION)
                                                           String sortingSourcePageUrl,
                                                       @RequestParam(value = CommonAttributeNameConstants.SEARCH, required = false)
                                                           String searchedText) {

        session.setAttribute(sortingOptionName, sortingOption);
        if (searchedText != null && !searchedText.isEmpty() && !searchedText.equals("null")) {
            redirectAttributes.addAttribute(CommonAttributeNameConstants.SEARCH, searchedText);
        }
        return "redirect:%s".formatted(sortingSourcePageUrl);
    }

    @PostMapping("/" + UrlPartConstants.MESSAGES)
    public String redirectSourcePageAfterSortingMessages(HttpSession session,
                                                         RedirectAttributes redirectAttributes,
                                                         MessageSortingOption sortingOption,
                                                         @RequestParam(SortingAttributeNameConstants.SORTING_OPTION_NAME)
                                                             String sortingOptionName,
                                                         @RequestParam(CommonAttributeNameConstants.SOURCE_PAGE_URL_WITHOUT_PAGINATION)
                                                             String sortingSourcePageUrl,
                                                         @RequestParam(value = CommonAttributeNameConstants.SEARCH, required = false)
                                                             String searchedText) {

        session.setAttribute(sortingOptionName, sortingOption);
        if (searchedText != null && !searchedText.isEmpty() && !searchedText.equals("null")) {
            redirectAttributes.addAttribute(CommonAttributeNameConstants.SEARCH, searchedText);
        }
        return "redirect:%s".formatted(sortingSourcePageUrl);
    }

    @PostMapping("/" + UrlPartConstants.BANS)
    public String redirectSourcePageAfterSortingBans(HttpSession session,
                                                     RedirectAttributes redirectAttributes,
                                                     BanSortingOption sortingOption,
                                                     @RequestParam(SortingAttributeNameConstants.SORTING_OPTION_NAME)
                                                         String sortingOptionName,
                                                     @RequestParam(CommonAttributeNameConstants.SOURCE_PAGE_URL_WITHOUT_PAGINATION)
                                                         String sortingSourcePageUrl,
                                                     @RequestParam(value = CommonAttributeNameConstants.SEARCH, required = false)
                                                         String searchedText) {

        session.setAttribute(sortingOptionName, sortingOption);
        if (searchedText != null && !searchedText.isEmpty() && !searchedText.equals("null")) {
            redirectAttributes.addAttribute(CommonAttributeNameConstants.SEARCH, searchedText);
        }
        return "redirect:%s".formatted(sortingSourcePageUrl);
    }

}
