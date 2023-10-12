package org.forum.main.controllers;

import jakarta.validation.Valid;
import org.forum.auxiliary.constants.ControllerBaseUrlConstants;
import org.forum.main.controllers.common.ConvenientController;
import org.forum.main.entities.Ban;
import org.forum.main.services.interfaces.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(ControllerBaseUrlConstants.FOR_USERS_ACTIONS_CONTROLLER)
public class UsersActivitiesController extends ConvenientController {

    private final UserService service;

    private final RoleService roleService;

    private final MessageService messageService;

    private final LikeService likeService;

    private final DislikeService dislikeService;

    private final SectionService sectionService;

    private final BanService banService;

    @Autowired
    public UsersActivitiesController(UserService service, RoleService roleService, MessageService messageService,
                                     LikeService likeService, DislikeService dislikeService,
                                     SectionService sectionService, BanService banService) {
        this.service = service;
        this.roleService = roleService;
        this.messageService = messageService;
        this.likeService = likeService;
        this.dislikeService = dislikeService;
        this.sectionService = sectionService;
        this.banService = banService;
    }

    @PostMapping("/assign-moder/{id}")
    public String redirectSamePageAfterAssigningModeration(@PathVariable("id") String pathId,
                                                           @RequestParam("sourcePage") String sourcePage) {
        Integer id = toNonNegativeInteger(pathId);
        service.changeRole(service.findById(id), roleService.findByName("ROLE_MODER"));
        return "redirect:" + sourcePage;
    }

    @PostMapping("/assign-admin/{id}")
    public String redirectSamePageAfterAssigningAdministration(@PathVariable("id") String pathId,
                                                               @RequestParam("sourcePage") String sourcePage) {
        Integer id = toNonNegativeInteger(pathId);
        service.changeRole(service.findById(id), roleService.findByName("ROLE_ADMIN"));
        return "redirect:" + sourcePage;
    }

    @PostMapping("/assign-user/{id}")
    public String redirectSamePageAfterAssigningUser(@PathVariable("id") String pathId,
                                                     @RequestParam("sourcePage") String sourcePage) {
        Integer id = toNonNegativeInteger(pathId);
        service.changeRole(service.findById(id), roleService.findByName("ROLE_USER"));
        return "redirect:" + sourcePage;
    }

    @PostMapping("/like/by{userWhoLikedId}")
    public String redirectSamePageAfterLike(@RequestParam("messageId") Long messageId,
                                            @RequestParam("isCancellation") boolean isCancellation,
                                            @RequestParam("sourcePage") String sourcePage,
                                            @PathVariable("userWhoLikedId") String pathUserWhoLikedId) {

        likeService.saveOrCancel(
                messageService.findById(messageId), service.findById(toNonNegativeInteger(pathUserWhoLikedId)), isCancellation
        );
        return "redirect:" + sourcePage;
    }

    @PostMapping("/dislike/by{userWhoDislikedId}")
    public String redirectSamePageAfterDislike(@RequestParam("messageId") Long messageId,
                                               @RequestParam("isCancellation") boolean isCancellation,
                                               @RequestParam("sourcePage") String sourcePage,
                                               @PathVariable("userWhoDislikedId") String pathUserWhoDislikedId) {

        dislikeService.saveOrCancel(
                messageService.findById(messageId), service.findById(toNonNegativeInteger(pathUserWhoDislikedId)), isCancellation
        );
        return "redirect:" + sourcePage;
    }

    @PostMapping("/ban")
    public String returnBanFormPage(Model model,
                                    Authentication authentication,
                                    @RequestParam("userId") String pathUserId) {

        Integer userId = toNonNegativeInteger(pathUserId);

        addForHeader(model, authentication, sectionService);
        add(model, "ban", banService.empty());
        add(model, "userId", userId);

        return "ban-form";
    }

    @PostMapping("/ban/process")
    public String redirectUserProfilePageAfterBan(Model model,
                                           Authentication authentication,
                                           @RequestParam("userId") String pathUserId,
                                           @RequestParam("userWhoAssignedId") String pathUserWhoAssignedId,
                                           @Valid Ban ban,
                                           BindingResult bindingResult) {

        Integer userId = toNonNegativeInteger(pathUserId);
        Integer userWhoAssignedId = toNonNegativeInteger(pathUserWhoAssignedId);

        if (banService.savingValidation(ban, bindingResult)) {

            addForHeader(model, authentication, sectionService);
            add(model, "ban", ban);
            return "ban-form";

        }

        banService.save(ban, service.findById(userId), service.findById(userWhoAssignedId));

        return "redirect:/users/" + ban.getUser().getId();
    }

    @PostMapping("/unban")
    public String redirectUserProfilePageAfterUnban(@RequestParam("userId") String pathUserId) {

        Integer userId = toNonNegativeInteger(pathUserId);

        banService.unban(service.findById(userId));

        return "redirect:/users/" + userId;
    }

}
