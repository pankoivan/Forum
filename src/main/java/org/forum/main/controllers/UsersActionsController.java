package org.forum.main.controllers;

import jakarta.validation.Valid;
import org.forum.auxiliary.constants.CommonAttributeNameConstants;
import org.forum.auxiliary.constants.url.ControllerBaseUrlConstants;
import org.forum.main.controllers.common.ConvenientController;
import org.forum.main.entities.Ban;
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

    private final SectionService sectionService;

    private final MessageService messageService;

    private final RoleService roleService;

    private final LikeService likeService;

    private final DislikeService dislikeService;

    private final BanService banService;

    @Autowired
    public UsersActionsController(UserService service, RoleService roleService, MessageService messageService,
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

    @PostMapping("/assign-user/{id}")
    @PreAuthorize("hasAnyAuthority('ASSIGN_REMOVE_MODERS', 'ASSIGN_REMOVE_ADMINS')")
    public String redirectSourcePageAfterAssigningUser(@PathVariable("id") String pathId,
                                                       @RequestParam(CommonAttributeNameConstants.SOURCE_PAGE_URL_WITH_PAGE)
                                                           String sourcePage) {

        Integer id = toNonNegativeInteger(pathId);
        service.changeRole(service.findById(id), roleService.findByName("ROLE_USER"));
        return "redirect:%s"
                .formatted(sourcePage);
    }

    @PostMapping("/assign-moder/{id}")
    @PreAuthorize("hasAuthority('ASSIGN_REMOVE_MODERS')")
    public String redirectSourcePageAfterAssigningModer(@PathVariable("id") String pathId,
                                                        @RequestParam(CommonAttributeNameConstants.SOURCE_PAGE_URL_WITH_PAGE)
                                                            String sourcePage) {

        Integer id = toNonNegativeInteger(pathId);
        service.changeRole(service.findById(id), roleService.findByName("ROLE_MODER"));
        return "redirect:%s"
                .formatted(sourcePage);
    }

    @PostMapping("/assign-admin/{id}")
    @PreAuthorize("hasAuthority('ASSIGN_REMOVE_ADMINS')")
    public String redirectSourcePageAfterAssigningAdmin(@PathVariable("id") String pathId,
                                                        @RequestParam(CommonAttributeNameConstants.SOURCE_PAGE_URL_WITH_PAGE)
                                                            String sourcePage) {

        Integer id = toNonNegativeInteger(pathId);
        service.changeRole(service.findById(id), roleService.findByName("ROLE_ADMIN"));
        return "redirect:%s"
                .formatted(sourcePage);
    }

    @PostMapping("/like/to{messageId}")
    @PreAuthorize("hasAuthority('LIKE')")
    public String redirectSourcePageAfterLike(Authentication authentication,
                                              @PathVariable("messageId") Long messageId,
                                              @RequestParam("isCancellation") boolean isCancellation,
                                              @RequestParam(CommonAttributeNameConstants.SOURCE_PAGE_URL_WITH_PAGE)
                                                  String sourcePage) {

        likeService.saveOrDelete(messageService.findById(messageId), extractCurrentUser(authentication), isCancellation);
        return "redirect:%s"
                .formatted(sourcePage);
    }

    @PostMapping("/dislike/to{messageId}")
    @PreAuthorize("hasAuthority('DISLIKE')")
    public String redirectSourcePageAfterDislike(Authentication authentication,
                                                 @PathVariable("messageId") Long messageId,
                                                 @RequestParam("isCancellation") boolean isCancellation,
                                                 @RequestParam(CommonAttributeNameConstants.SOURCE_PAGE_URL_WITH_PAGE)
                                                     String sourcePage) {

        dislikeService.saveOrDelete(messageService.findById(messageId), extractCurrentUser(authentication), isCancellation);
        return "redirect:%s"
                .formatted(sourcePage);
    }

    @PostMapping("/ban/{id}")
    @PreAuthorize("hasAuthority('BAN_AND_UNBAN')")
    public String returnBanFormPage(Model model,
                                    Authentication authentication,
                                    @PathVariable("id") String pathUserId,
                                    @RequestParam(CommonAttributeNameConstants.SOURCE_PAGE_URL_WITH_PAGE)
                                        String sourcePage) {

        Integer userId = toNonNegativeInteger(pathUserId);

        addForHeader(model, authentication, sectionService);
        add(model, "ban", banService.empty());
        add(model, "userId", userId);
        add(model, CommonAttributeNameConstants.SOURCE_PAGE_URL_WITH_PAGE, sourcePage);

        return "ban-form";
    }

    @PostMapping("/ban/process")
    @PreAuthorize("hasAuthority('BAN_AND_UNBAN')")
    public String redirectUserProfilePageAfterBan(Model model,
                                                  Authentication authentication,
                                                  @RequestParam("userId") String pathUserId,
                                                  @RequestParam("userWhoAssignedId") String pathUserWhoAssignedId,
                                                  @RequestParam(CommonAttributeNameConstants.SOURCE_PAGE_URL_WITH_PAGE)
                                                      String sourcePage,
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

        return "redirect:%s"
                .formatted(sourcePage);
    }

    @PostMapping("/unban/{id}")
    @PreAuthorize("hasAuthority('BAN_AND_UNBAN')")
    public String redirectUserProfilePageAfterUnban(@PathVariable("id") String pathUserId,
                                                    @RequestParam(CommonAttributeNameConstants.SOURCE_PAGE_URL_WITH_PAGE)
                                                        String sourcePage) {

        Integer userId = toNonNegativeInteger(pathUserId);

        banService.unban(service.findById(userId));

        return "redirect:%s"
                .formatted(sourcePage);
    }

}
