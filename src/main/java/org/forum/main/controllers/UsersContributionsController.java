package org.forum.main.controllers;

import jakarta.servlet.http.HttpSession;
import org.forum.auxiliary.constants.ControllerBaseUrlConstants;
import org.forum.auxiliary.sorting.enums.MessageSortingProperties;
import org.forum.auxiliary.sorting.options.MessageSortingOption;
import org.forum.main.controllers.common.ConvenientController;
import org.forum.main.entities.Message;
import org.forum.main.services.interfaces.MessageService;
import org.forum.main.services.interfaces.SectionService;
import org.forum.main.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(ControllerBaseUrlConstants.FOR_USERS_CONTRIBUTIONS_CONTROLLER)
public class UsersContributionsController extends ConvenientController {

    private final UserService service;

    private final MessageService messageService;

    private final SectionService sectionService;

    @Autowired
    public UsersContributionsController(UserService service, MessageService messageService,
                                        SectionService sectionService) {
        this.service = service;
        this.messageService = messageService;
        this.sectionService = sectionService;
    }

    @GetMapping("/messages/posted")
    public String redirectPostedMessagesPageWithPagination() {
        return "redirect:/users/{id}/contributions/messages/posted/page1";
    }

    @GetMapping("/messages/posted/page{pageNumber}")
    public String returnPostedMessagesPage(Model model,
                                           Authentication authentication,
                                           @SessionAttribute(value = "messageSortingOption", required = false)
                                               MessageSortingOption sortingOption,
                                           @PathVariable("id") String pathUserId,
                                           @PathVariable("pageNumber") String pathPageNumber) {

        Integer userId = toNonNegativeInteger(pathUserId);
        Integer pageNumber = toNonNegativeInteger(pathPageNumber);

        addForHeader(model, authentication, sectionService);
        add(model, "isForUserContributions", true);
        add(model, "messages", sortedPostedMessages(sortingOption,
                service.findById(userId).getId(), pageNumber));
        add(model, "pagesCount", messageService.pagesCount(service.findById(userId).getPostedMessages()));
        add(model, "currentPage", pageNumber);
        add(model, "sortingObject", service.emptySortingOption());
        add(model, "properties", MessageSortingProperties.values());
        add(model, "directions", Sort.Direction.values());
        add(model, "userId", pathUserId);

        return "messages";
    }

    /*@PostMapping("/messages/posted/page{pageNumber}/sort")
    public String redirectCurrentPageAfterSorting(HttpSession session, MessageSortingOption sortingOption) {
        session.setAttribute("messageSortingOption", sortingOption);
        return "redirect:/users/{id}/contributions/messages/posted/page{pageNumber}";
    }*/

    @GetMapping("/messages/liked")
    public String redirectLikedMessagesPageWithPagination() {
        return "redirect:/users/{id}/contributions/messages/liked/page1";
    }

    @GetMapping("/messages/liked/page{pageNumber}")
    public String returnLikedMessagesPage(Model model) {
        return "messages";
    }

    @GetMapping("/messages/disliked")
    public String redirectDislikedMessagesPageWithPagination() {
        return "redirect:/users/{id}/contributions/messages/disliked/page1";
    }

    @GetMapping("/messages/disliked/page{pageNumber}")
    public String returnDislikedMessagesPage(Model model) {
        return "messages";
    }

    @GetMapping("/topics/created")
    public String redirectCreatedTopicsPageWithPagination() {
        return "redirect:/users/{id}/contributions/topics/created/page1";
    }

    @GetMapping("/topics/created/page{pageNumber}")
    public String returnCreatedTopicsPage(Model model) {
        return "topics";
    }

    @GetMapping("/sections/created")
    public String redirectCreatedSectionsPageWithPagination() {
        return "redirect:/users/{id}/contributions/sections/created/page1";
    }

    @GetMapping("/sections/created/page{pageNumber}")
    public String returnCreatedSectionsPage() {
        return "sections";
    }

    private List<Message> sortedPostedMessages(MessageSortingOption sortingOption, Integer userId, Integer pageNumber) {
        return sortingOption != null
                ? messageService.onPage(messageService.findAllByUserIdSorted(userId, sortingOption), pageNumber)
                : messageService.onPage(messageService.findAllByUserIdSortedByDefault(userId), pageNumber);
    }

    private List<Message> sortedLikedMessages(MessageSortingOption sortingOption, Integer userId, Integer pageNumber) {
        return sortingOption != null
                ? messageService.onPage(messageService.findAllLikedByUserIdSorted(userId, sortingOption), pageNumber)
                : messageService.onPage(messageService.findAllLikedByUserIdSortedByDefault(userId), pageNumber);
    }

    private List<Message> sortedDislikedMessages(MessageSortingOption sortingOption, Integer userId, Integer pageNumber) {
        return sortingOption != null
                ? messageService.onPage(messageService.findAllDislikedByUserIdSorted(userId, sortingOption), pageNumber)
                : messageService.onPage(messageService.findAllDislikedByUserIdSortedByDefault(userId), pageNumber);
    }

}
