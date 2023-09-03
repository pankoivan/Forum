package org.forum.controllers.mvc.common;

import org.forum.services.interfaces.SectionService;
import org.forum.global.utils.AuthenticationUtils;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;

public abstract class ConvenientController {

    protected void add(Model model, String attributeName, Object attribute) {
        model.addAttribute(attributeName, attribute);
    }

    protected void addForHeader(Model model, Authentication authentication, SectionService service) {
        add(model, "currentUser", AuthenticationUtils.extractCurrentUserOrNull(authentication));
        add(model, "sections", service.findAll());
    }

}
