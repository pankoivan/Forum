package org.forum.main.controllers;

import org.forum.main.controllers.common.ConvenientController;
import org.forum.main.services.interfaces.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/users/activities")
public class UsersActivitiesController extends ConvenientController {

    private final UserService service;

    private final RoleService roleService;

    private final MessageService messageService;

    private final LikeService likeService;

    private final DislikeService dislikeService;

    @Autowired
    public UsersActivitiesController(UserService service, RoleService roleService, MessageService messageService,
                                     LikeService likeService, DislikeService dislikeService) {
        this.service = service;
        this.roleService = roleService;
        this.messageService = messageService;
        this.likeService = likeService;
        this.dislikeService = dislikeService;
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

}
