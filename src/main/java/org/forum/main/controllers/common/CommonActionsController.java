package org.forum.main.controllers.common;

import jakarta.servlet.http.HttpSession;
import org.forum.auxiliary.constants.ControllerBaseUrlConstants;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;

@Controller(ControllerBaseUrlConstants.FOR_COMMON_ACTIONS_CONTROLLER)
public class CommonActionsController {

    public String redirectSourcePageAfterSavingSortingOptionToSession(HttpSession session,
                                                                      Object sortingOption,
                                                                      @RequestParam("sortingOptionName")
                                                                          String sortingOptionName,
                                                                      @RequestParam("sourcePageUrl")
                                                                          String sourcePageUrl) {
        session.setAttribute("messageSortingOption", sortingOption);
        return "redirect:" + sourcePageUrl;
    }

}
