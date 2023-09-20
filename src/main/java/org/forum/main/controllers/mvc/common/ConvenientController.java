package org.forum.main.controllers.mvc.common;

import org.forum.auxiliary.exceptions.ControllerException;
import org.forum.auxiliary.exceptions.common.ForumCheckedException;
import org.forum.main.services.interfaces.SectionService;
import org.forum.auxiliary.utils.AuthenticationUtils;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;

public abstract class ConvenientController {

    protected void add(Model model, String attributeName, Object attribute) {
        model.addAttribute(attributeName, attribute);
    }

    protected void addForHeader(Model model, Authentication authentication, SectionService service) {
        try {
            add(model, "currentUser", AuthenticationUtils.extractCurrentUserOrNull(authentication));
            add(model, "sections", service.findAll());
        } catch (ForumCheckedException e) {
            throw new ControllerException("Current user cannot be extracted");
        }
    }

}
