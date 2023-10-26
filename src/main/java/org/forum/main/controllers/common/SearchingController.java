package org.forum.main.controllers.common;

import org.forum.auxiliary.constants.CommonAttributeNameConstants;
import org.forum.auxiliary.constants.url.ControllerBaseUrlConstants;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequestMapping(ControllerBaseUrlConstants.FOR_SEARCHING_CONTROLLER)
@PreAuthorize("permitAll()")
public class SearchingController {

    @PostMapping
    public String redirectSourcePageAfterSearching(RedirectAttributes redirectAttributes,
                                                   @RequestParam(CommonAttributeNameConstants.SEARCH)
                                                       String searchedText,
                                                   @RequestParam(CommonAttributeNameConstants.SOURCE_PAGE_URL_WITHOUT_PAGINATION)
                                                       String searchingSourcePageUrl) {

        redirectAttributes.addAllAttributes(Map.of(CommonAttributeNameConstants.SEARCH, searchedText));
        return "redirect:%s".formatted(searchingSourcePageUrl);
    }

}
