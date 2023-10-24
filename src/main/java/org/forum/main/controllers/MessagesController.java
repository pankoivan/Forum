package org.forum.main.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.forum.auxiliary.constants.CommonAttributeNameConstants;
import org.forum.auxiliary.constants.pagination.PaginationAttributeNameConstants;
import org.forum.auxiliary.constants.sorting.SortingAttributeNameConstants;
import org.forum.auxiliary.constants.url.ControllerBaseUrlConstants;
import org.forum.auxiliary.constants.sorting.SortingOptionNameConstants;
import org.forum.auxiliary.constants.url.UrlPartConstants;
import org.forum.auxiliary.sorting.options.MessageSortingOption;
import org.forum.auxiliary.sorting.enums.MessageSortingProperties;
import org.forum.auxiliary.utils.UrlUtils;
import org.forum.main.controllers.common.ConvenientController;
import org.forum.main.entities.Message;
import org.forum.main.services.interfaces.MessageService;
import org.forum.main.services.interfaces.SectionService;
import org.forum.main.services.interfaces.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping(ControllerBaseUrlConstants.FOR_MESSAGES_CONTROLLER)
public class MessagesController extends ConvenientController {

    private final SectionService sectionService;

    private final TopicService topicService;

    private final MessageService service;

    @Autowired
    public MessagesController(SectionService sectionService, TopicService topicService, MessageService service) {
        this.sectionService = sectionService;
        this.topicService = topicService;
        this.service = service;
    }

    @GetMapping
    public String redirectMessagesPageWithPagination(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAllAttributes(request.getParameterMap());
        return "redirect:%s/%s1".formatted(request.getRequestURI(), UrlPartConstants.PAGE);
    }

    @GetMapping("/" + UrlPartConstants.PAGE_PAGE_NUMBER_PATTERN)
    public String returnMessagesPage(HttpSession session,
                                     HttpServletRequest request,
                                     Model model,
                                     Authentication authentication,
                                     @SessionAttribute(value = SortingOptionNameConstants.FOR_MESSAGES_SORTING_OPTION, required = false)
                                         MessageSortingOption sortingOption,
                                     @SessionAttribute(value = "message", required = false) Message message,
                                     @SessionAttribute(value = "formSubmitButtonText", required = false)
                                         String formSubmitButtonText,
                                     @SessionAttribute(value = "formErrorMessage", required = false)
                                         String formErrorMessage,
                                     @SessionAttribute(value = "errorMessage", required = false)
                                         String errorMessage,
                                     @RequestParam(value = CommonAttributeNameConstants.SEARCH, required = false)
                                         String searchedText,
                                     @PathVariable(UrlPartConstants.SECTION_ID) String pathSectionId,
                                     @PathVariable(UrlPartConstants.TOPIC_ID) String pathTopicId,
                                     @PathVariable(UrlPartConstants.PAGE_NUMBER) String pathPageNumber) {

        Integer sectionId = toNonNegativeInteger(pathSectionId);
        Integer topicId = toNonNegativeInteger(pathTopicId);
        Integer pageNumber = toNonNegativeInteger(pathPageNumber);

        List<Message> messages = searchedAndSorted(sortingOption, searchedText, topicId);

        addForHeader(model, authentication, sectionService);
        add(model, "messages", service.onPage(messages, pageNumber));
        add(model, "sectionId", sectionId);
        add(model, "topicId", topicId);
        add(model, "topicName", topicService.findById(topicId).getName());
        add(model, "message", service.empty());
        add(model, "formSubmitButtonText", "Отправить сообщение");
        add(model, CommonAttributeNameConstants.IS_FOR_USER_CONTRIBUTIONS, false);
        add(model, CommonAttributeNameConstants.IS_EDIT_DELETE_BUTTONS_ENABLED, true);
        add(model, CommonAttributeNameConstants.IS_LIKE_DISLIKE_BUTTONS_ENABLED, true);
        add(model, CommonAttributeNameConstants.SOURCE_PAGE_URL_WITH_PAGE, request.getRequestURI());
        add(model, CommonAttributeNameConstants.SOURCE_PAGE_URL_WITHOUT_PAGE, removePagination(request.getRequestURI()));
        add(model, CommonAttributeNameConstants.REQUEST_PARAMETERS, makeParametersString(request.getParameterMap()));
        add(model, PaginationAttributeNameConstants.PAGES_COUNT, service.pagesCount(messages));
        add(model, PaginationAttributeNameConstants.CURRENT_PAGE, pageNumber);
        add(model, SortingAttributeNameConstants.SORTING_OBJECT, sortingOption == null ? service.emptySortingOption() : sortingOption);
        add(model, SortingAttributeNameConstants.SORTING_PROPERTIES, MessageSortingProperties.values());
        add(model, SortingAttributeNameConstants.SORTING_DIRECTIONS, Sort.Direction.values());
        add(model, SortingAttributeNameConstants.SORTING_OPTION_NAME, SortingOptionNameConstants.FOR_MESSAGES_SORTING_OPTION);
        add(model, SortingAttributeNameConstants.SORTING_SUBMIT_URL, concat(ControllerBaseUrlConstants.FOR_SORTING_CONTROLLER,
                UrlPartConstants.MESSAGES));

        if (message != null) {
            add(model, "message", message);
            session.removeAttribute("message");
        }
        if (formErrorMessage != null) {
            add(model, "formSubmitButtonText", formSubmitButtonText);
            add(model, "formError", formErrorMessage);
            session.removeAttribute("formSubmitButtonText");
            session.removeAttribute("formErrorMessage");
        }
        if (errorMessage != null) {
            add(model, "error", errorMessage);
            session.removeAttribute("errorMessage");
        }

        return "messages";
    }

    @PostMapping("/" + UrlPartConstants.PAGE_PAGE_NUMBER_PATTERN + "/save")
    public String redirectMessagesPageAfterSaving(HttpSession session,
                                                  Authentication authentication,
                                                  @Valid Message message,
                                                  BindingResult bindingResult,
                                                  @PathVariable(UrlPartConstants.TOPIC_ID) String pathTopicId) {

        Integer topicId = toNonNegativeInteger(pathTopicId);

        boolean isNew = service.isNew(message);

        if (service.savingValidation(message, bindingResult)) {
            session.setAttribute("message", message);
            session.setAttribute("formSubmitButtonText", isNew ? "Отправить сообщение" : "Сохранить изменения");
            session.setAttribute("formErrorMessage", service.anyError(bindingResult));
            return "redirect:%s/%s".formatted(ControllerBaseUrlConstants.FOR_MESSAGES_CONTROLLER, UrlPartConstants.PAGE_PAGE_NUMBER_PATTERN);
        }

        service.save(message, authentication, topicService.findById(topicId));
        return isNew
                ? "redirect:%s/%s%s"
                    .formatted(
                            ControllerBaseUrlConstants.FOR_MESSAGES_CONTROLLER,
                            UrlPartConstants.PAGE,
                            service.pagesCount(service.findAllByTopicId(topicId))
                    )
                : "redirect:%s/%s"
                    .formatted(ControllerBaseUrlConstants.FOR_MESSAGES_CONTROLLER, UrlPartConstants.PAGE_PAGE_NUMBER_PATTERN);

    }

    @PostMapping("/" + UrlPartConstants.PAGE_PAGE_NUMBER_PATTERN + "/edit/{id}")
    public String redirectMessagesPageForEditing(HttpSession session, @PathVariable("id") String pathId) {

        Long id = toNonNegativeLong(pathId);

        session.setAttribute("message", service.findById(id));
        session.setAttribute("formSubmitButtonText", "Сохранить изменения");

        return "redirect:%s/%s"
                .formatted(ControllerBaseUrlConstants.FOR_MESSAGES_CONTROLLER, UrlPartConstants.PAGE_PAGE_NUMBER_PATTERN);
    }

    @PostMapping("/" + UrlPartConstants.PAGE_PAGE_NUMBER_PATTERN + "/delete/{id}")
    public String redirectMessagesPageAfterDeleting(HttpSession session,
                                                    @PathVariable("id") String pathId,
                                                    @PathVariable(UrlPartConstants.TOPIC_ID) String pathTopicId,
                                                    @PathVariable(UrlPartConstants.PAGE_NUMBER) String pathPageNumber) {

        Long id = toNonNegativeLong(pathId);

        String msg = service.deletingValidation(service.findById(id));
        if (msg != null) {
            session.setAttribute("errorMessage", msg);
            return "redirect:%s/%s"
                    .formatted(ControllerBaseUrlConstants.FOR_MESSAGES_CONTROLLER, UrlPartConstants.PAGE_PAGE_NUMBER_PATTERN);
        }

        Integer topicId = toNonNegativeInteger(pathTopicId);
        Integer pagesCount = service.pagesCount(service.findAllByTopicId(topicId));
        Integer pageNumber = toNonNegativeInteger(pathPageNumber);

        service.deleteById(id);
        int newPagesCount = service.pagesCount(service.findAllByTopicId(topicId));
        return pageNumber.equals(pagesCount) && newPagesCount < pagesCount
                ? "redirect:%s/%s%s"
                    .formatted(
                            ControllerBaseUrlConstants.FOR_MESSAGES_CONTROLLER,
                            UrlPartConstants.PAGE,
                            newPagesCount
                    )
                : "redirect:%s/%s"
                    .formatted(ControllerBaseUrlConstants.FOR_MESSAGES_CONTROLLER, UrlPartConstants.PAGE_PAGE_NUMBER_PATTERN);
    }

    private List<Message> sorted(MessageSortingOption sortingOption, Integer topicId) {
        return sortingOption != null
                ? service.findAllByTopicIdSorted(topicId, sortingOption)
                : service.findAllByTopicIdSorted(topicId);
    }

    private List<Message> searchedAndSorted(MessageSortingOption sortingOption, String searchedText, Integer topicId) {
        return searchedText != null && !searchedText.isEmpty()
                ? service.search(sorted(sortingOption, topicId), searchedText)
                : sorted(sortingOption, topicId);
    }

}
