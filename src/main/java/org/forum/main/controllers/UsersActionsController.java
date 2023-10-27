package org.forum.main.controllers;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.forum.auxiliary.constants.CommonAttributeNameConstants;
import org.forum.auxiliary.constants.url.ControllerBaseUrlConstants;
import org.forum.main.controllers.common.ConvenientController;
import org.forum.main.entities.Ban;
import org.forum.main.entities.User;
import org.forum.main.services.interfaces.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(ControllerBaseUrlConstants.FOR_USERS_ACTIONS_CONTROLLER)
@PreAuthorize("isAuthenticated()")
public class UsersActionsController extends ConvenientController {

    private final UserService service;

    private final RoleService roleService;

    private final SectionService sectionService;

    private final MessageService messageService;

    private final LikeService likeService;

    private final DislikeService dislikeService;

    private final BanService banService;

    @Autowired
    public UsersActionsController(UserService service, RoleService roleService, SectionService sectionService,
                                  MessageService messageService, LikeService likeService, DislikeService dislikeService,
                                  BanService banService) {
        this.service = service;
        this.roleService = roleService;
        this.sectionService = sectionService;
        this.messageService = messageService;
        this.likeService = likeService;
        this.dislikeService = dislikeService;
        this.banService = banService;
    }

    @PostMapping("/assign-user/{id}")
    @PreAuthorize("hasAnyAuthority('ASSIGN_AND_REMOVE_MODERS', 'ASSIGN_AND_REMOVE_ADMINS')")
    public String redirectSourcePageAfterAssigningUser(@RequestParam(CommonAttributeNameConstants.SOURCE_PAGE_URL_WITH_PAGINATION)
                                                           String sourcePage,
                                                       @PathVariable("id") String pathId) {

        Integer id = toNonNegativeInteger(pathId);

        service.changeRole(service.findById(id), roleService.findByName("ROLE_USER"));
        return "redirect:%s".formatted(sourcePage);
    }

    @PostMapping("/assign-moder/{id}")
    @PreAuthorize("hasAuthority('ASSIGN_AND_REMOVE_MODERS')")
    public String redirectSourcePageAfterAssigningModer(@RequestParam(CommonAttributeNameConstants.SOURCE_PAGE_URL_WITH_PAGINATION)
                                                            String sourcePage,
                                                        @PathVariable("id") String pathId) {

        Integer id = toNonNegativeInteger(pathId);

        service.changeRole(service.findById(id), roleService.findByName("ROLE_MODER"));
        return "redirect:%s".formatted(sourcePage);
    }

    @PostMapping("/assign-admin/{id}")
    @PreAuthorize("hasAuthority('ASSIGN_AND_REMOVE_ADMINS')")
    public String redirectSourcePageAfterAssigningAdmin(@RequestParam(CommonAttributeNameConstants.SOURCE_PAGE_URL_WITH_PAGINATION)
                                                            String sourcePage,
                                                        @PathVariable("id") String pathId) {

        Integer id = toNonNegativeInteger(pathId);

        service.changeRole(service.findById(id), roleService.findByName("ROLE_ADMIN"));
        return "redirect:%s".formatted(sourcePage);
    }

    @PostMapping("/like/to{messageId}")
    @PreAuthorize("hasAuthority('LIKE')")
    public String redirectSourcePageAfterLike(Authentication authentication,
                                              @RequestParam("isCancellation") boolean isCancellation,
                                              @RequestParam(CommonAttributeNameConstants.SOURCE_PAGE_URL_WITH_PAGINATION)
                                                  String sourcePage,
                                              @PathVariable("messageId") Long messageId) {

        likeService.saveOrDelete(messageService.findById(messageId), extractCurrentUser(authentication), isCancellation);
        return "redirect:%s".formatted(sourcePage);
    }

    @PostMapping("/dislike/to{messageId}")
    @PreAuthorize("hasAuthority('DISLIKE')")
    public String redirectSourcePageAfterDislike(Authentication authentication,
                                                 @RequestParam("isCancellation") boolean isCancellation,
                                                 @RequestParam(CommonAttributeNameConstants.SOURCE_PAGE_URL_WITH_PAGINATION)
                                                     String sourcePage,
                                                 @PathVariable("messageId") Long messageId) {

        dislikeService.saveOrDelete(messageService.findById(messageId), extractCurrentUser(authentication), isCancellation);
        return "redirect:%s".formatted(sourcePage);
    }

    @GetMapping("/ban/{id}")
    @PreAuthorize("hasAuthority('BAN_AND_UNBAN')")
    public String returnBanFormPage(HttpSession session,
                                    Model model,
                                    Authentication authentication,
                                    @SessionAttribute(value = "errorMessage", required = false) String errorMessage,
                                    @SessionAttribute(value = "ban", required = false) Ban ban,
                                    @PathVariable("id") String pathUserId) {

        Integer userId = toNonNegativeInteger(pathUserId);

        addForHeader(model, authentication, sectionService);
        add(model, "ban", banService.empty());
        add(model, "userId", userId);

        if (errorMessage != null) {
            add(model, "error", errorMessage);
            add(model, "ban", ban);
            session.removeAttribute("errorMessage");
            session.removeAttribute("ban");
        }

        return "ban-form";
    }

    @PostMapping("/ban/process")
    @PreAuthorize("hasAuthority('BAN_AND_UNBAN')")
    public String redirectUserProfilePageAfterBan(HttpSession session,
                                                  Authentication authentication,
                                                  @RequestParam("userId") String pathUserId,
                                                  @Valid Ban ban,
                                                  BindingResult bindingResult) {

        Integer userId = toNonNegativeInteger(pathUserId);
        User userWhoAssigned = extractCurrentUser(authentication);

        if (banService.savingValidation(ban, bindingResult)) {
            session.setAttribute("errorMessage", banService.anyError(bindingResult));
            session.setAttribute("ban", ban);
            return "redirect:%s/ban/%s".formatted(ControllerBaseUrlConstants.FOR_USERS_ACTIONS_CONTROLLER, userId);
        }

        banService.save(ban, service.findById(userId), userWhoAssigned);
        return "redirect:%s/%s".formatted(ControllerBaseUrlConstants.FOR_USERS_CONTROLLER, userId);
    }

    @GetMapping("/unban/{id}")
    @PreAuthorize("hasAuthority('BAN_AND_UNBAN')")
    public String redirectUserProfilePageAfterUnban(@PathVariable("id") String pathUserId) {

        Integer userId = toNonNegativeInteger(pathUserId);

        banService.unban(service.findById(userId));
        return "redirect:%s/%s".formatted(ControllerBaseUrlConstants.FOR_USERS_CONTROLLER, userId);
    }

}
