package org.forum.main.controllers;

import jakarta.validation.Valid;
import org.forum.auxiliary.constants.ControllerBaseUrlConstants;
import org.forum.auxiliary.constants.SortingOptionNameConstants;
import org.forum.auxiliary.constants.UrlPartConstants;
import org.forum.auxiliary.sorting.options.MessageSortingOption;
import org.forum.auxiliary.sorting.enums.MessageSortingProperties;
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
    public String redirectMessagesPageWithPagination() {
        return "redirect:%s/page1"
                .formatted(ControllerBaseUrlConstants.FOR_MESSAGES_CONTROLLER);
    }

    @GetMapping("/page{pageNumber}")
    public String returnMessagesPage(Model model,
                                     Authentication authentication,
                                     @SessionAttribute(value = SortingOptionNameConstants.FOR_MESSAGE_SORTING_OPTION,
                                             required = false)
                                         MessageSortingOption sortingOption,
                                     @PathVariable("sectionId") String pathSectionId,
                                     @PathVariable("topicId") String pathTopicId,
                                     @PathVariable("pageNumber") String pathPageNumber) {

        Integer sectionId = toNonNegativeInteger(pathSectionId);
        Integer topicId = toNonNegativeInteger(pathTopicId);
        Integer pageNumber = toNonNegativeInteger(pathPageNumber);

        addForHeader(model, authentication, sectionService);

        add(model, "messages", sorted(sortingOption, topicId, pageNumber));
        add(model, "sectionId", sectionId);
        add(model, "topicId", topicId);
        add(model, "topicName", topicService.findById(topicId).getName());
        add(model, "message", service.empty());
        add(model, "formSubmitButtonText", "Отправить сообщение");
        add(model, "pagesCount", service.pagesCount(service.findAllByTopicId(topicId)));
        add(model, "currentPage", pageNumber);
        add(model, "sortingObject", sortingOption == null ? service.emptySortingOption() : sortingOption);
        add(model, "properties", MessageSortingProperties.values());
        add(model, "directions", Sort.Direction.values());
        add(model, "sortingOptionName", SortingOptionNameConstants.FOR_MESSAGE_SORTING_OPTION);
        add(model, "sortingSubmitUrl", ControllerBaseUrlConstants.FOR_SORTING_CONTROLLER +
                UrlPartConstants.MESSAGES);
        add(model, "sortingSourcePageUrl", replacePatternParts(
                ControllerBaseUrlConstants.FOR_MESSAGES_CONTROLLER,
                sectionId,
                topicId
        ));

        return "messages";
    }

    @PostMapping("/page{pageNumber}/inner/save")
    public String redirectMessagesPageAfterSaving(Model model,
                                                  Authentication authentication,
                                                  @Valid Message message,
                                                  BindingResult bindingResult,
                                                  @SessionAttribute(value = SortingOptionNameConstants.FOR_MESSAGE_SORTING_OPTION,
                                                          required = false)
                                                      MessageSortingOption sortingOption,
                                                  @PathVariable("sectionId") String pathSectionId,
                                                  @PathVariable("topicId") String pathTopicId,
                                                  @PathVariable("pageNumber") String pathPageNumber) {

        Integer sectionId = toNonNegativeInteger(pathSectionId);
        Integer topicId = toNonNegativeInteger(pathTopicId);
        Integer pageNumber = toNonNegativeInteger(pathPageNumber);

        boolean isNew = service.isNew(message);
        if (service.savingValidation(message, bindingResult)) {

            addForHeader(model, authentication, sectionService);

            add(model, "messages", sorted(sortingOption, topicId, pageNumber));
            add(model, "sectionId", sectionId);
            add(model, "topicId", topicId);
            add(model, "topicName", topicService.findById(topicId).getName());
            add(model, "message", message);
            add(model, "formSubmitButtonText", isNew ? "Отправить сообщение" : "Сохранить изменения");
            add(model, "pagesCount", service.pagesCount(service.findAllByTopicId(topicId)));
            add(model, "currentPage", pageNumber);
            add(model, "sortingObject", sortingOption == null ? service.emptySortingOption() : sortingOption);
            add(model, "properties", MessageSortingProperties.values());
            add(model, "directions", Sort.Direction.values());
            add(model, "sortingOptionName", SortingOptionNameConstants.FOR_MESSAGE_SORTING_OPTION);
            add(model, "sortingSubmitUrl", ControllerBaseUrlConstants.FOR_SORTING_CONTROLLER +
                    UrlPartConstants.MESSAGES);
            add(model, "sortingSourcePageUrl", replacePatternParts(
                    ControllerBaseUrlConstants.FOR_MESSAGES_CONTROLLER,
                    sectionId,
                    topicId
            ));
            add(model, "formError", service.extractAnySingleError(bindingResult));

            return "messages";
        }

        service.save(message, authentication, topicService.findById(topicId));

        return isNew
                ? "redirect:%s/page%s"
                    .formatted(
                            ControllerBaseUrlConstants.FOR_MESSAGES_CONTROLLER,
                            service.pagesCount(service.findAllByTopicId(topicId))
                    )
                : "redirect:%s/page{pageNumber}"
                    .formatted(ControllerBaseUrlConstants.FOR_MESSAGES_CONTROLLER);

    }

    @PostMapping("/page{pageNumber}/inner/edit/{id}")
    public String returnMessagesPageForEditing(Model model,
                                               Authentication authentication,
                                               @SessionAttribute(value = SortingOptionNameConstants.FOR_MESSAGE_SORTING_OPTION,
                                                       required = false)
                                                   MessageSortingOption sortingOption,
                                               @PathVariable("id") String pathId,
                                               @PathVariable("sectionId") String pathSectionId,
                                               @PathVariable("topicId") String pathTopicId,
                                               @PathVariable("pageNumber") String pathPageNumber) {

        Long id = toNonNegativeLong(pathId);
        Integer sectionId = toNonNegativeInteger(pathSectionId);
        Integer topicId = toNonNegativeInteger(pathTopicId);
        Integer pageNumber = toNonNegativeInteger(pathPageNumber);

        addForHeader(model, authentication, sectionService);

        add(model, "messages", sorted(sortingOption, topicId, pageNumber));
        add(model, "sectionId", sectionId);
        add(model, "topicId", topicId);
        add(model, "topicName", topicService.findById(topicId).getName());
        add(model, "message", service.findById(id));
        add(model, "formSubmitButtonText", "Сохранить изменения");
        add(model, "pagesCount", service.pagesCount(service.findAllByTopicId(topicId)));
        add(model, "currentPage", pageNumber);
        add(model, "sortingObject", sortingOption == null ? service.emptySortingOption() : sortingOption);
        add(model, "properties", MessageSortingProperties.values());
        add(model, "directions", Sort.Direction.values());
        add(model, "sortingOptionName", SortingOptionNameConstants.FOR_MESSAGE_SORTING_OPTION);
        add(model, "sortingSubmitUrl", ControllerBaseUrlConstants.FOR_SORTING_CONTROLLER +
                UrlPartConstants.MESSAGES);
        add(model, "sortingSourcePageUrl", replacePatternParts(
                ControllerBaseUrlConstants.FOR_MESSAGES_CONTROLLER,
                sectionId,
                topicId
        ));

        return "messages";
    }

    @PostMapping("/page{pageNumber}/inner/delete/{id}")
    public String redirectMessagesPageAfterDeleting(Model model,
                                                    Authentication authentication,
                                                    @SessionAttribute(value = SortingOptionNameConstants.FOR_MESSAGE_SORTING_OPTION,
                                                            required = false)
                                                        MessageSortingOption sortingOption,
                                                    @PathVariable("id") String pathId,
                                                    @PathVariable("sectionId") String pathSectionId,
                                                    @PathVariable("topicId") String pathTopicId,
                                                    @PathVariable("pageNumber") String pathPageNumber) {

        Long id = toNonNegativeLong(pathId);
        Integer sectionId = toNonNegativeInteger(pathSectionId);
        Integer topicId = toNonNegativeInteger(pathTopicId);
        Integer pageNumber = toNonNegativeInteger(pathPageNumber);

        int pagesCount = service.pagesCount(service.findAllByTopicId(topicId));

        String msg = service.deletingValidation(service.findById(id));
        if (msg != null) {

            addForHeader(model, authentication, sectionService);
            add(model, "sectionId", sectionId);
            add(model, "topicId", topicId);
            add(model, "topicName", topicService.findById(topicId).getName());
            add(model, "messages", sorted(sortingOption, topicId, pageNumber));
            add(model, "message", service.empty());
            add(model, "formSubmitButtonText", "Отправить сообщение");
            add(model, "pagesCount", pagesCount);
            add(model, "currentPage", pageNumber);
            add(model, "sortingObject", sortingOption == null ? service.emptySortingOption() : sortingOption);
            add(model, "properties", MessageSortingProperties.values());
            add(model, "directions", Sort.Direction.values());
            add(model, "sortingOptionName", SortingOptionNameConstants.FOR_MESSAGE_SORTING_OPTION);
            add(model, "sortingSubmitUrl", ControllerBaseUrlConstants.FOR_SORTING_CONTROLLER +
                    UrlPartConstants.MESSAGES);
            add(model, "sortingSourcePageUrl", replacePatternParts(
                    ControllerBaseUrlConstants.FOR_MESSAGES_CONTROLLER,
                    sectionId,
                    topicId
            ));
            add(model, "error", msg);

            return "messages";
        }

        service.deleteById(id);

        int newPagesCount = service.pagesCount(service.findAllByTopicId(topicId));

        return pageNumber.equals(pagesCount) && newPagesCount < pagesCount
                ? "redirect:%s/page%s"
                    .formatted(ControllerBaseUrlConstants.FOR_MESSAGES_CONTROLLER, newPagesCount)
                : "redirect:%s/page{pageNumber}"
                    .formatted(ControllerBaseUrlConstants.FOR_MESSAGES_CONTROLLER);
    }

    private List<Message> sorted(MessageSortingOption sortingOption, Integer topicId, Integer pageNumber) {
        return sortingOption != null
                ? service.onPage(service.findAllByTopicIdSorted(topicId, sortingOption), pageNumber)
                : service.onPage(service.findAllByTopicIdSortedByDefault(topicId), pageNumber);
    }

}
