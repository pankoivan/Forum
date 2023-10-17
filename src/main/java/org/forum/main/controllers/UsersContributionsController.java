package org.forum.main.controllers;

import org.forum.auxiliary.constants.url.ControllerBaseUrlConstants;
import org.forum.auxiliary.constants.SortingOptionNameConstants;
import org.forum.auxiliary.constants.url.UrlPartConstants;
import org.forum.auxiliary.exceptions.ControllerException;
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

    private static final String CREATED = "created";

    private static final String POSTED = "posted";

    private static final String LIKED = "liked";

    private static final String DISLIKED = "disliked";

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

    @GetMapping("/" + UrlPartConstants.MESSAGES + "/" + POSTED)
    public String redirectPostedMessagesPageWithPagination() {
        return "redirect:%s/%s/%s/%s1"
                .formatted(
                        ControllerBaseUrlConstants.FOR_USERS_CONTRIBUTIONS_CONTROLLER,
                        UrlPartConstants.MESSAGES,
                        POSTED,
                        UrlPartConstants.PAGE
                );
    }

    @GetMapping("/" + UrlPartConstants.MESSAGES + "/" + LIKED)
    public String redirectLikedMessagesPageWithPagination() {
        return "redirect:%s/%s/%s/%s1"
                .formatted(
                        ControllerBaseUrlConstants.FOR_USERS_CONTRIBUTIONS_CONTROLLER,
                        UrlPartConstants.MESSAGES,
                        LIKED,
                        UrlPartConstants.PAGE
                );
    }

    @GetMapping("/" + UrlPartConstants.MESSAGES + "/" + DISLIKED)
    public String redirectDislikedMessagesPageWithPagination() {
        return "redirect:%s/%s/%s/%s1"
                .formatted(
                        ControllerBaseUrlConstants.FOR_USERS_CONTRIBUTIONS_CONTROLLER,
                        UrlPartConstants.MESSAGES,
                        DISLIKED,
                        UrlPartConstants.PAGE
                );
    }

    @GetMapping("/" + UrlPartConstants.MESSAGES + "/{whichMessages}/" + UrlPartConstants.PAGE_PAGE_NUMBER_PATTERN)
    public String returnPostedMessagesPage(Model model,
                                           Authentication authentication,
                                           @SessionAttribute(value = SortingOptionNameConstants.FOR_MESSAGE_SORTING_OPTION,
                                                   required = false)
                                               MessageSortingOption sortingOption,
                                           @PathVariable(UrlPartConstants.ID) String pathUserId,
                                           @PathVariable("whichMessages") String whichMessages,
                                           @PathVariable(UrlPartConstants.PAGE_NUMBER) String pathPageNumber) {

        Integer userId = toNonNegativeInteger(pathUserId);
        Integer pageNumber = toNonNegativeInteger(pathPageNumber);

        List<Message> messages = mySwitch(whichMessages, sortingOption, userId);

        addForHeader(model, authentication, sectionService);
        add(model, "isForUserContributions", true);
        add(model, "messages", messageService.onPage(messages, pageNumber));
        add(model, "pagesCount", messageService.pagesCount(messages));
        add(model, "currentPage", pageNumber);
        add(model, "paginationUrl", replacePatternParts(
                ControllerBaseUrlConstants.FOR_USERS_CONTRIBUTIONS_CONTROLLER +
                        addStartSlash(UrlPartConstants.MESSAGES) +
                        addStartSlash(whichMessages),
                userId
        ));
        add(model, "sortingObject", sortingOption == null ? service.emptySortingOption() : sortingOption);
        add(model, "properties", MessageSortingProperties.values());
        add(model, "directions", Sort.Direction.values());
        add(model, "sortingOptionName", SortingOptionNameConstants.FOR_MESSAGE_SORTING_OPTION);
        add(model, "sortingSubmitUrl", ControllerBaseUrlConstants.FOR_SORTING_CONTROLLER +
                addStartSlash(UrlPartConstants.MESSAGES));
        add(model, "sortingSourcePageUrl", replacePatternParts(
                ControllerBaseUrlConstants.FOR_USERS_CONTRIBUTIONS_CONTROLLER +
                        addStartSlash(UrlPartConstants.MESSAGES)
                        + addStartSlash(whichMessages),
                userId
        ));

        return "messages";
    }

    private List<Message> mySwitch(String whichMessages, MessageSortingOption sortingOption, Integer userId) {
        return switch (whichMessages) {
            case POSTED -> sortedPostedMessages(sortingOption, userId);
            case LIKED -> sortedLikedMessages(sortingOption, userId);
            case DISLIKED -> sortedDislikedMessages(sortingOption, userId);
            default -> throw new ControllerException("Unknown URL part: \"%s\"".formatted(whichMessages));
        };
    }

    private List<Message> sortedPostedMessages(MessageSortingOption sortingOption, Integer userId) {
        return sortingOption != null
                ? messageService.findAllByUserIdSorted(userId, sortingOption)
                : messageService.findAllByUserIdSortedByDefault(userId);
    }

    private List<Message> sortedLikedMessages(MessageSortingOption sortingOption, Integer userId) {
        return sortingOption != null
                ? messageService.findAllLikedByUserIdSorted(userId, sortingOption)
                : messageService.findAllLikedByUserIdSortedByDefault(userId);
    }

    private List<Message> sortedDislikedMessages(MessageSortingOption sortingOption, Integer userId) {
        return sortingOption != null
                ? messageService.findAllDislikedByUserIdSorted(userId, sortingOption)
                : messageService.findAllDislikedByUserIdSortedByDefault(userId);
    }

}
