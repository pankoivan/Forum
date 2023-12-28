package org.forum.main.controllers;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.forum.auxiliary.constants.url.ControllerBaseUrlConstants;
import org.forum.main.controllers.common.ConvenientController;
import org.forum.main.entities.User;
import org.forum.main.entities.enums.Gender;
import org.forum.main.services.interfaces.SectionService;
import org.forum.main.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping(ControllerBaseUrlConstants.FOR_AUTH_CONTROLLER)
@PreAuthorize("permitAll()")
public class AuthController extends ConvenientController {

    private final SectionService sectionService;

    private final UserService service;

    @Autowired
    public AuthController(SectionService sectionService, UserService service) {
        this.sectionService = sectionService;
        this.service = service;
    }

    @GetMapping("/login")
    public String returnLoginPage(Model model, Authentication authentication) {
        if (extractCurrentUserOrNull(authentication) != null) {
            return "redirect:%s".formatted(ControllerBaseUrlConstants.FOR_INDEX_CONTROLLER);
        }
        addForHeader(model, authentication, sectionService);
        return "login";
    }

    @GetMapping("/registration")
    public String returnRegistrationPage(HttpSession session,
                                         Model model,
                                         Authentication authentication,
                                         @SessionAttribute(value = "user", required = false) User user,
                                         @SessionAttribute(value = "avatar", required = false) MultipartFile avatar,
                                         @SessionAttribute(value = "error", required = false) String error) {

        addForHeader(model, authentication, sectionService);
        model.addAttribute("user", service.empty());
        model.addAttribute("genders", Gender.values());
        model.addAttribute("user", service.empty());

        if (error != null) {
            model.addAttribute("user", user);
            model.addAttribute("avatar", avatar);
            model.addAttribute("error", error);
            session.removeAttribute("user");
            session.removeAttribute("avatar");
            session.removeAttribute("error");
        }

        return "registration";
    }

    @PostMapping("/registration/processing")
    public String redirectLoginPageAfterRegistration(HttpSession session,
                                                     RedirectAttributes redirectAttributes,
                                                     @Valid User user,
                                                     BindingResult bindingResult,
                                                     MultipartFile avatar) {

        if (service.savingValidation(user, bindingResult)) {
            session.setAttribute("user", user);
            session.setAttribute("avatar", avatar);
            session.setAttribute("error", service.anyError(bindingResult));
            return "redirect:%s/registration".formatted(ControllerBaseUrlConstants.FOR_AUTH_CONTROLLER);
        }

        service.save(user, avatar);
        redirectAttributes.addAttribute("registered", true);
        return "redirect:%s/login".formatted(ControllerBaseUrlConstants.FOR_AUTH_CONTROLLER);
    }

}
