package org.forum.main.controllers.common;

import org.forum.auxiliary.exceptions.ControllerException;
import org.forum.auxiliary.exceptions.common.AuxiliaryInstrumentsException;
import org.forum.auxiliary.utils.PathVariableUtils;
import org.forum.auxiliary.utils.UrlUtils;
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
            add(model, "headerSections", service.findAllSortedByDefault());
        } catch (AuxiliaryInstrumentsException e) {
            throw new ControllerException("Current user cannot be extracted", e);
        }
    }

    protected Integer toNonNegativeInteger(String pathVariable) {
        try {
            return PathVariableUtils.toNonNegativeInteger(pathVariable);
        } catch (AuxiliaryInstrumentsException e) {
            throw new ControllerException(e.getMessage(), e);
        }
    }

    protected Long toNonNegativeLong(String pathVariable) {
        try {
            return PathVariableUtils.toNonNegativeLong(pathVariable);
        } catch (AuxiliaryInstrumentsException e) {
            throw new ControllerException(e.getMessage(), e);
        }
    }

    protected String replacePatternPart(String sourceUrl, Object replacePart) {
        return PathVariableUtils.replacePatternPart(sourceUrl, replacePart);
    }

    protected String replacePatternParts(String sourceUrl, Object ... replaceParts) throws ControllerException {
        try {
            return PathVariableUtils.replacePatternParts(sourceUrl, replaceParts);
        } catch (AuxiliaryInstrumentsException e) {
            throw new ControllerException(e.getMessage(), e);
        }
    }

    protected String addStartSlash(String urlPart) {
        return UrlUtils.addStartSlash(urlPart);
    }

    protected String removeStartSlash(String urlPart) {
        return UrlUtils.removeStartSlash(urlPart);
    }

}
