package org.forum.main.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.forum.auxiliary.constants.CommonAttributeNameConstants;
import org.forum.auxiliary.constants.pagination.PaginationAttributeNameConstants;
import org.forum.auxiliary.constants.sorting.SortingAttributeNameConstants;
import org.forum.auxiliary.constants.url.ControllerBaseUrlConstants;
import org.forum.auxiliary.constants.sorting.SortingOptionNameConstants;
import org.forum.auxiliary.constants.url.UrlPartConstants;
import org.forum.auxiliary.exceptions.ControllerException;
import org.forum.auxiliary.sorting.enums.MessageSortingProperties;
import org.forum.auxiliary.sorting.enums.SectionSortingProperties;
import org.forum.auxiliary.sorting.enums.TopicSortingProperties;
import org.forum.auxiliary.sorting.options.MessageSortingOption;
import org.forum.auxiliary.sorting.options.SectionSortingOption;
import org.forum.auxiliary.sorting.options.TopicSortingOption;
import org.forum.main.controllers.common.ConvenientController;
import org.forum.main.entities.Message;
import org.forum.main.entities.Section;
import org.forum.main.entities.Topic;
import org.forum.main.services.interfaces.MessageService;
import org.forum.main.services.interfaces.SectionService;
import org.forum.main.services.interfaces.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping(ControllerBaseUrlConstants.FOR_USERS_CONTRIBUTIONS_CONTROLLER)
@PreAuthorize("isAuthenticated()")
public class UsersContributionsController extends ConvenientController {

    private static final String CREATED = "created";

    private static final String POSTED = "posted";

    private static final String LIKED = "liked";

    private static final String DISLIKED = "disliked";

    private final SectionService sectionService;

    private final TopicService topicService;

    private final MessageService messageService;

    @Autowired
    public UsersContributionsController(SectionService sectionService, TopicService topicService, MessageService messageService) {
        this.sectionService = sectionService;
        this.topicService = topicService;
        this.messageService = messageService;
    }

    @GetMapping("/" + UrlPartConstants.SECTIONS + "/" + CREATED)
    public String redirectCreatedSectionsPageWithPagination(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAllAttributes(request.getParameterMap());
        return "redirect:%s/%s1".formatted(request.getRequestURI(), UrlPartConstants.PAGE);
    }

    @GetMapping("/" + UrlPartConstants.SECTIONS + "/" + CREATED + "/" + UrlPartConstants.PAGE_PAGE_NUMBER_PATTERN)
    public String returnCreatedSectionsPage(HttpServletRequest request,
                                            Model model,
                                            Authentication authentication,
                                            @SessionAttribute(value = SortingOptionNameConstants.FOR_SECTIONS_SORTING_OPTION,
                                                    required = false)
                                                SectionSortingOption sortingOption,
                                            @PathVariable(UrlPartConstants.ID) String pathUserId,
                                            @PathVariable(UrlPartConstants.PAGE_NUMBER) String pathPageNumber) {

        Integer userId = toNonNegativeInteger(pathUserId);
        Integer pageNumber = toNonNegativeInteger(pathPageNumber);

        List<Section> sections = sortedCreatedSections(sortingOption, userId);

        addForHeader(model, authentication, sectionService);
        add(model, "isForUserContributions", true);
        add(model, "sections", sectionService.onPage(sections, pageNumber));
        add(model, CommonAttributeNameConstants.IS_EDIT_DELETE_BUTTONS_ENABLED, false);
        currentPage(model, request.getRequestURI());
        pagination(model, sectionService.pagesCount(sections), pageNumber);
        sectionsSorting(model, sortingOption);

        return "sections";
    }

    @GetMapping("/" + UrlPartConstants.TOPICS + "/" + CREATED)
    public String redirectCreatedTopicsPageWithPagination(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAllAttributes(request.getParameterMap());
        return "redirect:%s/%s1".formatted(request.getRequestURI(), UrlPartConstants.PAGE);
    }

    @GetMapping("/" + UrlPartConstants.TOPICS + "/" + CREATED + "/" + UrlPartConstants.PAGE_PAGE_NUMBER_PATTERN)
    public String returnCreatedTopicsPage(HttpServletRequest request,
                                          Model model,
                                          Authentication authentication,
                                          @SessionAttribute(value = SortingOptionNameConstants.FOR_TOPICS_SORTING_OPTION,
                                                  required = false)
                                          TopicSortingOption sortingOption,
                                          @PathVariable(UrlPartConstants.ID) String pathUserId,
                                          @PathVariable(UrlPartConstants.PAGE_NUMBER) String pathPageNumber) {

        Integer userId = toNonNegativeInteger(pathUserId);
        Integer pageNumber = toNonNegativeInteger(pathPageNumber);

        List<Topic> topics = sortedCreatedTopics(sortingOption, userId);

        addForHeader(model, authentication, sectionService);
        add(model, "isForUserContributions", true);
        add(model, "topics", topicService.onPage(topics, pageNumber));
        add(model, CommonAttributeNameConstants.IS_EDIT_DELETE_BUTTONS_ENABLED, false);
        currentPage(model, request.getRequestURI());
        pagination(model, topicService.pagesCount(topics), pageNumber);
        topicsSorting(model, sortingOption);

        return "topics";
    }

    @GetMapping("/" + UrlPartConstants.MESSAGES + "/" + POSTED)
    public String redirectPostedMessagesPageWithPagination(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAllAttributes(request.getParameterMap());
        return "redirect:%s/%s1".formatted(request.getRequestURI(), UrlPartConstants.PAGE);
    }

    @GetMapping("/" + UrlPartConstants.MESSAGES + "/" + LIKED)
    public String redirectLikedMessagesPageWithPagination(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAllAttributes(request.getParameterMap());
        return "redirect:%s/%s1".formatted(request.getRequestURI(), UrlPartConstants.PAGE);
    }

    @GetMapping("/" + UrlPartConstants.MESSAGES + "/" + DISLIKED)
    public String redirectDislikedMessagesPageWithPagination(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAllAttributes(request.getParameterMap());
        return "redirect:%s/%s1".formatted(request.getRequestURI(), UrlPartConstants.PAGE);
    }

    @GetMapping("/" + UrlPartConstants.MESSAGES + "/{whichMessages}/" + UrlPartConstants.PAGE_PAGE_NUMBER_PATTERN)
    public String returnMessagesPage(HttpServletRequest request,
                                     Model model,
                                     Authentication authentication,
                                     @SessionAttribute(value = SortingOptionNameConstants.FOR_MESSAGES_SORTING_OPTION,
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
        add(model, CommonAttributeNameConstants.IS_LIKE_DISLIKE_BUTTONS_ENABLED, false);
        add(model, CommonAttributeNameConstants.IS_EDIT_DELETE_BUTTONS_ENABLED, false);
        currentPage(model, request.getRequestURI());
        pagination(model, messageService.pagesCount(messages), pageNumber);
        messagesSorting(model, sortingOption);

        return "messages";
    }

    private void currentPage(Model model, String currentUrl) {
        add(model, CommonAttributeNameConstants.SOURCE_PAGE_URL_WITH_PAGINATION, currentUrl);
        add(model, CommonAttributeNameConstants.SOURCE_PAGE_URL_WITHOUT_PAGINATION, removePagination(currentUrl));
    }

    private void pagination(Model model, Integer pagesCount, Integer currentPage) {
        add(model, PaginationAttributeNameConstants.PAGES_COUNT, pagesCount);
        add(model, PaginationAttributeNameConstants.CURRENT_PAGE, currentPage);
    }

    private void sectionsSorting(Model model, SectionSortingOption sortingOption) {

        add(model, SortingAttributeNameConstants.SORTING_OBJECT,
                sortingOption == null ? sectionService.emptySortingOption() : sortingOption);

        add(model, SortingAttributeNameConstants.SORTING_PROPERTIES,
                SectionSortingProperties.values());

        add(model, SortingAttributeNameConstants.SORTING_DIRECTIONS,
                Sort.Direction.values());

        add(model, SortingAttributeNameConstants.SORTING_OPTION_NAME,
                SortingOptionNameConstants.FOR_SECTIONS_SORTING_OPTION);

        add(model, SortingAttributeNameConstants.SORTING_SUBMIT_URL,
                concat(ControllerBaseUrlConstants.FOR_SORTING_CONTROLLER, UrlPartConstants.SECTIONS));
    }

    private void topicsSorting(Model model, TopicSortingOption sortingOption) {

        add(model, SortingAttributeNameConstants.SORTING_OBJECT,
                sortingOption == null ? topicService.emptySortingOption() : sortingOption);

        add(model, SortingAttributeNameConstants.SORTING_PROPERTIES,
                TopicSortingProperties.values());

        add(model, SortingAttributeNameConstants.SORTING_DIRECTIONS,
                Sort.Direction.values());

        add(model, SortingAttributeNameConstants.SORTING_OPTION_NAME,
                SortingOptionNameConstants.FOR_TOPICS_SORTING_OPTION);

        add(model, SortingAttributeNameConstants.SORTING_SUBMIT_URL,
                concat(ControllerBaseUrlConstants.FOR_SORTING_CONTROLLER, UrlPartConstants.TOPICS));
    }

    private void messagesSorting(Model model, MessageSortingOption sortingOption) {

        add(model, SortingAttributeNameConstants.SORTING_OBJECT,
                sortingOption == null ? messageService.emptySortingOption() : sortingOption);

        add(model, SortingAttributeNameConstants.SORTING_PROPERTIES,
                MessageSortingProperties.values());

        add(model, SortingAttributeNameConstants.SORTING_DIRECTIONS,
                Sort.Direction.values());

        add(model, SortingAttributeNameConstants.SORTING_OPTION_NAME,
                SortingOptionNameConstants.FOR_MESSAGES_SORTING_OPTION);

        add(model, SortingAttributeNameConstants.SORTING_SUBMIT_URL,
                concat(ControllerBaseUrlConstants.FOR_SORTING_CONTROLLER, UrlPartConstants.MESSAGES));
    }

    private List<Section> sortedCreatedSections(SectionSortingOption sortingOption, Integer userId) {
        return sortingOption != null
                ? sectionService.findAllByUserIdSorted(userId, sortingOption)
                : sectionService.findAllByUserIdSorted(userId);
    }

    private List<Topic> sortedCreatedTopics(TopicSortingOption sortingOption, Integer userId) {
        return sortingOption != null
                ? topicService.findAllByUserIdSorted(userId, sortingOption)
                : topicService.findAllByUserIdSorted(userId);
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
                : messageService.findAllByUserIdSorted(userId);
    }

    private List<Message> sortedLikedMessages(MessageSortingOption sortingOption, Integer userId) {
        return sortingOption != null
                ? messageService.findAllLikedByUserIdSorted(userId, sortingOption)
                : messageService.findAllLikedByUserIdSorted(userId);
    }

    private List<Message> sortedDislikedMessages(MessageSortingOption sortingOption, Integer userId) {
        return sortingOption != null
                ? messageService.findAllDislikedByUserIdSorted(userId, sortingOption)
                : messageService.findAllDislikedByUserIdSorted(userId);
    }

}
