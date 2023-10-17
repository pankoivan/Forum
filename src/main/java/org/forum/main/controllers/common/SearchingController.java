package org.forum.main.controllers.common;

import org.forum.auxiliary.constants.url.ControllerBaseUrlConstants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(ControllerBaseUrlConstants.FOR_SEARCHING_CONTROLLER)
public class SearchingController {

    @PostMapping
    public String redirectSourcePageAfterSearching(@RequestParam("searchedText") String searchedText,
                                                   @RequestParam("searchingSourcePageUrl") String searchingSourcePageUrl) {

        return "redirect:%s?search=%s"
                .formatted(searchingSourcePageUrl, searchedText);
    }

}
