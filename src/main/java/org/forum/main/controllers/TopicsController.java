package org.forum.main.controllers;

import jakarta.validation.Valid;
import org.forum.auxiliary.constants.url.ControllerBaseUrlConstants;
import org.forum.auxiliary.constants.SortingOptionNameConstants;
import org.forum.auxiliary.constants.url.UrlPartConstants;
import org.forum.auxiliary.sorting.enums.TopicSortingProperties;
import org.forum.auxiliary.sorting.options.TopicSortingOption;
import org.forum.main.controllers.common.ConvenientController;
import org.forum.main.entities.Topic;
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
@RequestMapping(ControllerBaseUrlConstants.FOR_TOPICS_CONTROLLER)
public class TopicsController extends ConvenientController {

    private final SectionService sectionService;

    private final TopicService service;

    @Autowired
    public TopicsController(SectionService sectionService, TopicService service) {
        this.sectionService = sectionService;
        this.service = service;
    }

    @GetMapping
    public String redirectTopicsPageWithPagination() {
        return "redirect:%s/%s1"
                .formatted(ControllerBaseUrlConstants.FOR_TOPICS_CONTROLLER, UrlPartConstants.PAGE);
    }

    @GetMapping("/" + UrlPartConstants.PAGE_PAGE_NUMBER_PATTERN)
    public String returnTopicsPage(Model model,
                                   Authentication authentication,
                                   @SessionAttribute(value = SortingOptionNameConstants.FOR_TOPIC_SORTING_OPTION,
                                           required = false)
                                       TopicSortingOption sortingOption,
                                   @PathVariable(UrlPartConstants.SECTION_ID) String pathSectionId,
                                   @PathVariable(UrlPartConstants.PAGE_NUMBER) String pathPageNumber) {

        Integer sectionId = toNonNegativeInteger(pathSectionId);
        Integer pageNumber = toNonNegativeInteger(pathPageNumber);

        List<Topic> topics = sorted(sortingOption, sectionId);

        addForHeader(model, authentication, sectionService);
        add(model, "page", "topics");
        add(model, "topics", service.onPage(topics, pageNumber));
        add(model, "sectionId", sectionId);
        add(model, "sectionName", sectionService.findById(sectionId).getName());
        add(model, "pagesCount", service.pagesCount(topics));
        add(model, "currentPage", pageNumber);
        add(model, "paginationUrl", replacePatternParts(
                ControllerBaseUrlConstants.FOR_TOPICS_CONTROLLER,
                sectionId
        ));
        add(model, "sortingObject", sortingOption == null ? service.emptySortingOption() : sortingOption);
        add(model, "properties", TopicSortingProperties.values());
        add(model, "directions", Sort.Direction.values());
        add(model, "sortingOptionName", SortingOptionNameConstants.FOR_TOPIC_SORTING_OPTION);
        add(model, "sortingSubmitUrl", ControllerBaseUrlConstants.FOR_SORTING_CONTROLLER +
                addStartSlash(UrlPartConstants.TOPICS));
        add(model, "sortingSourcePageUrl", replacePatternParts(
                ControllerBaseUrlConstants.FOR_TOPICS_CONTROLLER,
                sectionId
        ));

        return "topics";
    }

    @GetMapping("/create")
    public String returnTopicFormPageForCreating(Model model,
                                                 Authentication authentication,
                                                 @PathVariable(UrlPartConstants.SECTION_ID) String pathSectionId) {

        Integer sectionId = toNonNegativeInteger(pathSectionId);

        addForHeader(model, authentication, sectionService);
        add(model, "sectionId", sectionId);
        add(model, "object", service.empty());
        add(model, "formSubmitButtonText", "Создать тему");

        return "topic-form";
    }

    @PostMapping("/inner/save")
    public String redirectTopicsPageAfterSaving(Model model,
                                                Authentication authentication,
                                                @Valid Topic topic,
                                                BindingResult bindingResult,
                                                @PathVariable(UrlPartConstants.SECTION_ID) String pathSectionId) {

        Integer sectionId = toNonNegativeInteger(pathSectionId);

        if (service.savingValidation(topic, bindingResult)) {

            addForHeader(model, authentication, sectionService);
            add(model, "object", topic);
            add(model, "sectionId", sectionId);
            add(model, "formSubmitButtonText", service.isNew(topic) ? "Создать тему" : "Сохранить");
            add(model, "error", service.extractAnySingleError(bindingResult));

            return "topic-form";
        }

        service.save(topic, authentication, sectionService.findById(sectionId));

        return "redirect:%s"
                .formatted(ControllerBaseUrlConstants.FOR_TOPICS_CONTROLLER);
    }

    @PostMapping("/inner/edit/{id}")
    public String returnTopicFormPageForEditing(Model model,
                                                Authentication authentication,
                                                @PathVariable("id") String pathId,
                                                @PathVariable(UrlPartConstants.SECTION_ID) String pathSectionId) {

        Integer id = toNonNegativeInteger(pathId);
        Integer sectionId = toNonNegativeInteger(pathSectionId);

        addForHeader(model, authentication, sectionService);
        add(model, "object", service.findById(id));
        add(model, "sectionId", sectionId);
        add(model, "formSubmitButtonText", "Сохранить");

        return "topic-form";
    }

    @PostMapping("/inner/delete/{id}")
    public String redirectTopicsPageAfterDeleting(Model model,
                                                  Authentication authentication,
                                                  @SessionAttribute(value = SortingOptionNameConstants.FOR_TOPIC_SORTING_OPTION,
                                                          required = false)
                                                      TopicSortingOption sortingOption,
                                                  @PathVariable("id") String pathId,
                                                  @PathVariable(UrlPartConstants.SECTION_ID) String pathSectionId) {

        Integer id = toNonNegativeInteger(pathId);
        Integer sectionId = toNonNegativeInteger(pathSectionId);

        String msg = service.deletingValidation(service.findById(id));
        if (msg != null) {

            List<Topic> topics = sorted(sortingOption, sectionId);

            addForHeader(model, authentication, sectionService);
            add(model, "page", "topics");
            add(model, "topics", service.onPage(topics, 1));
            add(model, "sectionId", sectionId);
            add(model, "sectionName", sectionService.findById(sectionId).getName());
            add(model, "pagesCount", service.pagesCount(topics));
            add(model, "currentPage", 1);
            add(model, "paginationUrl", replacePatternParts(
                    ControllerBaseUrlConstants.FOR_TOPICS_CONTROLLER,
                    sectionId
            ));
            add(model, "sortingObject", sortingOption == null ? service.emptySortingOption() : sortingOption);
            add(model, "properties", TopicSortingProperties.values());
            add(model, "directions", Sort.Direction.values());
            add(model, "sortingOptionName", SortingOptionNameConstants.FOR_TOPIC_SORTING_OPTION);
            add(model, "sortingSubmitUrl", ControllerBaseUrlConstants.FOR_SORTING_CONTROLLER +
                    addStartSlash(UrlPartConstants.TOPICS));
            add(model, "sortingSourcePageUrl", replacePatternParts(
                    ControllerBaseUrlConstants.FOR_TOPICS_CONTROLLER,
                    sectionId
            ));
            add(model, "error", msg);

            return "topics";
        }

        service.deleteById(id);

        return "redirect:%s"
                .formatted(ControllerBaseUrlConstants.FOR_TOPICS_CONTROLLER);
    }

    private List<Topic> sorted(TopicSortingOption sortingOption, Integer sectionId) {
        return sortingOption != null
                ? service.findAllBySectionIdSorted(sectionId, sortingOption)
                : service.findAllBySectionIdSortedByDefault(sectionId);
    }

}
