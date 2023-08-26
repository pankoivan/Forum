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

    default void add(Model model, String attributeName1, Object attribute1, String attributeName2, Object attribute2) {
        add(model, attributeName1, attribute1);
        model.addAttribute(attributeName2, attribute2);
    }

    default void add(Model model, String attributeName1, Object attribute1, String attributeName2, Object attribute2,
             String attributeName3, Object attribute3) {

        add(model, attributeName1, attribute1, attributeName2, attribute2);
        model.addAttribute(attributeName3, attribute3);
    }

    default void add(Model model, String attributeName1, Object attribute1, String attributeName2, Object attribute2,
             String attributeName3, Object attribute3, String attributeName4, Object attribute4) {

        add(model, attributeName1, attribute1, attributeName2, attribute2, attributeName3, attribute3);
        model.addAttribute(attributeName4, attribute4);
    }

    default void add(Model model, String attributeName1, Object attribute1, String attributeName2, Object attribute2,
                     String attributeName3, Object attribute3, String attributeName4, Object attribute4,
                     String attributeName5, Object attribute5) {

        add(model, attributeName1, attribute1, attributeName2, attribute2, attributeName3, attribute3,
                attributeName4, attribute4);
        model.addAttribute(attributeName5, attribute5);
    }

    default void add(Model model, String attributeName1, Object attribute1, String attributeName2, Object attribute2,
                     String attributeName3, Object attribute3, String attributeName4, Object attribute4,
                     String attributeName5, Object attribute5, String attributeName6, Object attribute6) {

        add(model, attributeName1, attribute1, attributeName2, attribute2, attributeName3, attribute3,
                attributeName4, attribute4, attributeName5, attribute5);
        model.addAttribute(attributeName6, attribute6);
    }

}
