package org.forum.main.controllers.common;

import jakarta.servlet.http.HttpSession;
import org.forum.auxiliary.constants.ControllerBaseUrlConstants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(ControllerBaseUrlConstants.FOR_COMMON_ACTIONS_CONTROLLER)
public class CommonActionsController {

    @PostMapping("/sort")
    public String redirectSourcePageAfterSorting(HttpSession session,
                                                 Object sortingOption,
                                                 @RequestParam("sortingOptionName") String sortingOptionName,
                                                 @RequestParam("sortingSourcePageUrl") String sortingSourcePageUrl) {

        session.setAttribute(sortingOptionName, sortingOption);
        return "redirect:%s"
                .formatted(sortingSourcePageUrl);
    }

    @PostMapping("/search")
    public String redirectSourcePageAfterSearching(@RequestParam("searchedText") String searchedText,
                                                   @RequestParam("searchingSourcePageUrl") String searchingSourcePageUrl) {

        return "redirect:%s?search=%s"
                .formatted(searchingSourcePageUrl, searchedText);
    }

}
