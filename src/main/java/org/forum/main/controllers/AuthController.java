package org.forum.main.controllers;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import
        org.forum.auxiliary.constants.url.ControllerBaseUrlConstants;
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
import org.springframework.web.multipart.MultipartFile;

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
    public String returnRegistrationPage(Model model, Authentication authentication) {
        addForHeader(model, authentication, sectionService);
        model.addAttribute("user", service.empty());
        model.addAttribute("genders", Gender.values());
        model.addAttribute("user", service.empty());
        return "registration";
    }

    @PostMapping("/registration/processing")
    public String redirectLoginPageAfterRegistration(HttpSession session,
                                                     @Valid User user,
                                                     BindingResult bindingResult,
                                                     MultipartFile img) {

        if (service.savingValidation(user, bindingResult)) {
            session.setAttribute("user", user);
            session.setAttribute("img", img);
            session.setAttribute("error", service.anyError(bindingResult));
            return "redirect:%s/registration".formatted(ControllerBaseUrlConstants.FOR_AUTH_CONTROLLER);
        }

        // на логин-странице сделать надпись о том, что пользователь был зарегистрирован

        service.save(user, img);
        return "redirect:%s/login".formatted(ControllerBaseUrlConstants.FOR_AUTH_CONTROLLER);
    }

}
