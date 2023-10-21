package org.forum.main.controllers.common;

import org.forum.auxiliary.constants.CommonAttributeNameConstants;
import org.forum.auxiliary.constants.url.ControllerBaseUrlConstants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URI;

@Controller
@RequestMapping(ControllerBaseUrlConstants.FOR_SEARCHING_CONTROLLER)
public class SearchingController {

    @PostMapping
    public String redirectSourcePageAfterSearching(@RequestParam(CommonAttributeNameConstants.SEARCHED_TEXT)
                                                       String searchedText,
                                                   @RequestParam(CommonAttributeNameConstants.SOURCE_PAGE_URL_WITHOUT_PAGE)
                                                       String searchingSourcePageUrl) {

        return "redirect:%s"
                .formatted(URI.create("%s?%s=%s"
                        .formatted(
                                searchingSourcePageUrl,
                                CommonAttributeNameConstants.SEARCHED_TEXT,
                                searchedText
                        )).toASCIIString());
    }

}
