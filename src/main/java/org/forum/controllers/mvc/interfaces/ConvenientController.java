package org.forum.controllers.mvc.interfaces;

import org.forum.utils.AuthenticationUtils;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;

public interface ConvenientController {

    default void addCurrentUser(Model model, Authentication authentication) {
        model.addAttribute("currentUser", AuthenticationUtils.extractCurrentUserOrNull(authentication));
    }

    default void add(Model model, String attributeName, Object attribute) {
        model.addAttribute(attributeName, attribute);
    }

}
