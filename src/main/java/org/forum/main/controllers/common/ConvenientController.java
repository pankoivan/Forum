package org.forum.main.controllers.common;

import org.forum.auxiliary.exceptions.ControllerException;
import org.forum.auxiliary.exceptions.common.AuxiliaryInstrumentsException;
import org.forum.auxiliary.utils.UrlPathVariableUtils;
import org.forum.auxiliary.utils.UrlUtils;
import org.forum.main.entities.User;
import org.forum.main.services.interfaces.SectionService;
import org.forum.auxiliary.utils.AuthenticationUtils;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;

import java.util.Map;

public abstract class ConvenientController {

    protected void add(Model model, String attributeName, Object attribute) {
        model.addAttribute(attributeName, attribute);
    }

    protected void addForHeader(Model model, Authentication authentication, SectionService service) {
        add(model, "currentUser", extractCurrentUserOrNull(authentication));
        add(model, "headerSections", service.findAllSorted());
    }

    protected User extractCurrentUser(Authentication authentication) {
        try {
            return AuthenticationUtils.extractCurrentUser(authentication);
        } catch (AuxiliaryInstrumentsException e) {
            throw new ControllerException(e.getMessage(), e);
        }
    }

    protected User extractCurrentUserOrNull(Authentication authentication) {
        try {
            return AuthenticationUtils.extractCurrentUserOrNull(authentication);
        } catch (AuxiliaryInstrumentsException e) {
            throw new ControllerException(e.getMessage(), e);
        }
    }

    protected Integer toNonNegativeInteger(String pathVariable) {
        try {
            return UrlPathVariableUtils.toNonNegativeInteger(pathVariable);
        } catch (AuxiliaryInstrumentsException e) {
            throw new ControllerException(e.getMessage(), e);
        }
    }

    protected Long toNonNegativeLong(String pathVariable) {
        try {
            return UrlPathVariableUtils.toNonNegativeLong(pathVariable);
        } catch (AuxiliaryInstrumentsException e) {
            throw new ControllerException(e.getMessage(), e);
        }
    }

    protected String concat(String ... urlParts) {
        return UrlUtils.concat(urlParts);
    }

    protected String removePagination(String url) {
        return UrlUtils.removePagination(url);
    }

    protected String makeParametersString(Map<String, String[]> parameterMap) {
        return UrlUtils.makeParametersString(parameterMap);
    }

}
