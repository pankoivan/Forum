package org.forum.main.controllers.common;

import jakarta.servlet.http.HttpServletRequest;
import org.forum.auxiliary.constants.CommonAttributeNameConstants;
import org.forum.auxiliary.constants.url.ControllerBaseUrlConstants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URI;
import java.util.Map;

@Controller
@RequestMapping(ControllerBaseUrlConstants.FOR_SEARCHING_CONTROLLER)
public class SearchingController {

    @PostMapping
    public String redirectSourcePageAfterSearching(RedirectAttributes redirectAttributes,
                                                   @RequestParam(CommonAttributeNameConstants.SEARCH)
                                                       String searchedText,
                                                   @RequestParam(CommonAttributeNameConstants.SOURCE_PAGE_URL_WITHOUT_PAGE)
                                                       String searchingSourcePageUrl) {

        redirectAttributes.addAllAttributes(Map.of("search", searchedText));
        /*return "redirect:%s"
                .formatted(URI.create("%s?%s=%s"
                        .formatted(
                                searchingSourcePageUrl,
                                CommonAttributeNameConstants.SEARCH,
                                searchedText
                        )).toASCIIString());*/
        return "redirect:%s"
                .formatted(searchingSourcePageUrl);
    }

}
