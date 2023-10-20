package org.forum.main.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.forum.auxiliary.constants.CommonAttributeNameConstants;
import org.forum.auxiliary.constants.pagination.PaginationAttributeNameConstants;
import org.forum.auxiliary.constants.sorting.SortingAttributeNameConstants;
import org.forum.auxiliary.constants.url.ControllerBaseUrlConstants;
import org.forum.auxiliary.constants.sorting.SortingOptionNameConstants;
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
    public String redirectTopicsPageWithPagination(HttpServletRequest request) {
        return "redirect:%s/%s1"
                .formatted(request.getRequestURI(), UrlPartConstants.PAGE);
    }

    @GetMapping("/" + UrlPartConstants.PAGE_PAGE_NUMBER_PATTERN)
    public String returnTopicsPage(HttpServletRequest request,
                                   Model model,
                                   Authentication authentication,
                                   @SessionAttribute(value = SortingOptionNameConstants.FOR_TOPICS_SORTING_OPTION,
                                           required = false)
                                       TopicSortingOption sortingOption,
                                   @PathVariable(UrlPartConstants.SECTION_ID) String pathSectionId,
                                   @PathVariable(UrlPartConstants.PAGE_NUMBER) String pathPageNumber) {

        Integer sectionId = toNonNegativeInteger(pathSectionId);
        Integer pageNumber = toNonNegativeInteger(pathPageNumber);

        List<Topic> topics = sorted(sortingOption, sectionId);

        addForHeader(model, authentication, sectionService);
        add(model, "isForUserContributions", false);
        add(model, "page", "topics");
        add(model, "topics", service.onPage(topics, pageNumber));
        add(model, "sectionId", sectionId);
        add(model, "sectionName", sectionService.findById(sectionId).getName());
        add(model, CommonAttributeNameConstants.IS_EDIT_DELETE_BUTTONS_ENABLED, true);
        currentPage(model, request.getRequestURI());
        pagination(model, service.pagesCount(topics), pageNumber);
        sorting(model, sortingOption);

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

    @PostMapping("/save")
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
            add(model, "error", service.anyError(bindingResult));

            return "topic-form";
        }

        service.save(topic, authentication, sectionService.findById(sectionId));

        return "redirect:%s"
                .formatted(ControllerBaseUrlConstants.FOR_TOPICS_CONTROLLER);
    }

    @PostMapping("/edit/{id}")
    public String returnTopicFormPageForEditing(Model model,
                                                Authentication authentication,
                                                @PathVariable(UrlPartConstants.SECTION_ID) String pathSectionId,
                                                @PathVariable("id") String pathId) {

        Integer id = toNonNegativeInteger(pathId);
        Integer sectionId = toNonNegativeInteger(pathSectionId);

        addForHeader(model, authentication, sectionService);
        add(model, "object", service.findById(id));
        add(model, "sectionId", sectionId);
        add(model, "formSubmitButtonText", "Сохранить");

        return "topic-form";
    }

    @PostMapping("/delete/{id}")
    public String redirectTopicsPageAfterDeleting(HttpServletRequest request,
                                                  Model model,
                                                  Authentication authentication,
                                                  @SessionAttribute(value = SortingOptionNameConstants.FOR_TOPICS_SORTING_OPTION,
                                                          required = false)
                                                      TopicSortingOption sortingOption,
                                                  @PathVariable(UrlPartConstants.SECTION_ID) String pathSectionId,
                                                  @PathVariable("id") String pathId) {

        Integer id = toNonNegativeInteger(pathId);
        Integer sectionId = toNonNegativeInteger(pathSectionId);

        String msg = service.deletingValidation(service.findById(id));
        if (msg != null) {

            List<Topic> topics = sorted(sortingOption, sectionId);

            addForHeader(model, authentication, sectionService);
            add(model, "isForUserContributions", false);
            add(model, "page", "topics");
            add(model, "topics", service.onPage(topics, 1));
            add(model, "sectionId", sectionId);
            add(model, "sectionName", sectionService.findById(sectionId).getName());
            add(model, CommonAttributeNameConstants.IS_EDIT_DELETE_BUTTONS_ENABLED, true);
            currentPage(model, request.getRequestURI());
            pagination(model, service.pagesCount(topics), 1);
            sorting(model, sortingOption);
            add(model, "error", msg);

            return "topics";
        }

        service.deleteById(id);

        return "redirect:%s"
                .formatted(ControllerBaseUrlConstants.FOR_TOPICS_CONTROLLER);
    }

    private void currentPage(Model model, String currentUrl) {
        add(model, CommonAttributeNameConstants.SOURCE_PAGE_URL_WITH_PAGE, currentUrl);
        add(model, CommonAttributeNameConstants.SOURCE_PAGE_URL_WITHOUT_PAGE, removePage(currentUrl));
    }

    private void pagination(Model model, Integer pagesCount, Integer currentPage) {
        add(model, PaginationAttributeNameConstants.PAGES_COUNT, pagesCount);
        add(model, PaginationAttributeNameConstants.CURRENT_PAGE, currentPage);
    }

    private void sorting(Model model, TopicSortingOption sortingOption) {

        add(model, SortingAttributeNameConstants.SORTING_OBJECT,
                sortingOption == null ? service.emptySortingOption() : sortingOption);

        add(model, SortingAttributeNameConstants.SORTING_PROPERTIES,
                TopicSortingProperties.values());

        add(model, SortingAttributeNameConstants.SORTING_DIRECTIONS,
                Sort.Direction.values());

        add(model, SortingAttributeNameConstants.SORTING_OPTION_NAME,
                SortingOptionNameConstants.FOR_TOPICS_SORTING_OPTION);

        add(model, SortingAttributeNameConstants.SORTING_SUBMIT_URL,
                concat(ControllerBaseUrlConstants.FOR_SORTING_CONTROLLER, UrlPartConstants.TOPICS));
    }

    private List<Topic> sorted(TopicSortingOption sortingOption, Integer sectionId) {
        return sortingOption != null
                ? service.findAllBySectionIdSorted(sectionId, sortingOption)
                : service.findAllBySectionIdSorted(sectionId);
    }

}
