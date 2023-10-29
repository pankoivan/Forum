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
import org.forum.auxiliary.sorting.properties.TopicSortingProperties;
import org.forum.auxiliary.sorting.options.TopicSortingOption;
import org.forum.main.controllers.common.ConvenientController;
import org.forum.main.entities.Section;
import org.forum.main.entities.Topic;
import org.forum.main.services.interfaces.SectionService;
import org.forum.main.services.interfaces.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping(ControllerBaseUrlConstants.FOR_TOPICS_CONTROLLER)
@PreAuthorize("permitAll()")
public class TopicsController extends ConvenientController {

    private final SectionService sectionService;

    private final TopicService service;

    @Autowired
    public TopicsController(SectionService sectionService, TopicService service) {
        this.sectionService = sectionService;
        this.service = service;
    }

    @GetMapping
    public String redirectTopicsPageWithPagination(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAllAttributes(request.getParameterMap());
        return "redirect:%s/%s1".formatted(request.getRequestURI(), UrlPartConstants.PAGE);
    }

    @GetMapping("/" + UrlPartConstants.PAGE_PAGE_NUMBER_PATTERN)
    public String returnTopicsPage(HttpSession session,
                                   HttpServletRequest request,
                                   Model model,
                                   Authentication authentication,
                                   @SessionAttribute(value = SortingOptionNameConstants.FOR_TOPICS_SORTING_OPTION, required = false)
                                       TopicSortingOption sortingOption,
                                   @SessionAttribute(value = "errorMessage", required = false) String errorMessage,
                                   @RequestParam(value = CommonAttributeNameConstants.SEARCH, required = false)
                                       String searchedText,
                                   @PathVariable(UrlPartConstants.SECTION_ID) String pathSectionId,
                                   @PathVariable(UrlPartConstants.PAGE_NUMBER) String pathPageNumber) {

        Integer sectionId = toNonNegativeInteger(pathSectionId);
        Integer pageNumber = toNonNegativeInteger(pathPageNumber);

        List<Topic> topics = searchedAndSorted(sortingOption, searchedText, sectionId);
        Section section = sectionService.findById(sectionId);

        addForHeader(model, authentication, sectionService);
        add(model, "topics", service.onPage(topics, pageNumber));
        add(model, "sectionId", sectionId);
        add(model, "sectionName", section.getName());
        add(model, CommonAttributeNameConstants.PAGE, "topics");
        add(model, CommonAttributeNameConstants.TITLE, "Темы раздела \"%s\" (стр. %s)".formatted(section.getName(), pageNumber));
        add(model, CommonAttributeNameConstants.IS_FOR_USER_CONTRIBUTIONS, false);
        add(model, CommonAttributeNameConstants.IS_EDIT_DELETE_BUTTONS_ENABLED, true);
        add(model, CommonAttributeNameConstants.SOURCE_PAGE_URL_WITH_PAGINATION, request.getRequestURI());
        add(model, CommonAttributeNameConstants.SOURCE_PAGE_URL_WITHOUT_PAGINATION, removePagination(request.getRequestURI()));
        add(model, CommonAttributeNameConstants.REQUEST_PARAMETERS, makeParametersString(request.getParameterMap()));
        add(model, PaginationAttributeNameConstants.PAGES_COUNT, service.pagesCount(topics));
        add(model, PaginationAttributeNameConstants.CURRENT_PAGE, pageNumber);
        add(model, SortingAttributeNameConstants.SORTING_OBJECT, sortingOption == null ? service.emptySortingOption() : sortingOption);
        add(model, SortingAttributeNameConstants.SORTING_PROPERTIES, TopicSortingProperties.values());
        add(model, SortingAttributeNameConstants.SORTING_DIRECTIONS, Sort.Direction.values());
        add(model, SortingAttributeNameConstants.SORTING_OPTION_NAME, SortingOptionNameConstants.FOR_TOPICS_SORTING_OPTION);
        add(model, SortingAttributeNameConstants.SORTING_SUBMIT_URL, concat(ControllerBaseUrlConstants.FOR_SORTING_CONTROLLER,
                UrlPartConstants.TOPICS));

        if (errorMessage != null) {
            add(model, "error", errorMessage);
            session.removeAttribute("errorMessage");
        }

        return "topics";
    }

    @GetMapping("/create")
    @PreAuthorize("hasAuthority('WORK_WITH_OWN_TOPICS')")
    public String returnTopicFormPageForCreating(HttpSession session,
                                                 Model model,
                                                 Authentication authentication,
                                                 @SessionAttribute(value = "object", required = false) Topic object,
                                                 @SessionAttribute(value = "formSubmitButtonText", required = false)
                                                     String formSubmitButtonText,
                                                 @SessionAttribute(value = "errorMessage", required = false)
                                                     String errorMessage,
                                                 @PathVariable(UrlPartConstants.SECTION_ID) String pathSectionId) {

        Integer sectionId = toNonNegativeInteger(pathSectionId);

        addForHeader(model, authentication, sectionService);
        add(model, "sectionId", sectionId);
        add(model, "object", service.empty());
        add(model, "formSubmitButtonText", "Создать тему");

        if (object != null) {
            add(model, "object", object);
            add(model, "formSubmitButtonText", formSubmitButtonText);
            if (errorMessage != null) {
                add(model, "error", errorMessage);
            }
            session.removeAttribute("object");
            session.removeAttribute("formSubmitButtonText");
            session.removeAttribute("errorMessage");
        }

        return "topic-form";
    }

    @PostMapping("/save")
    @PreAuthorize("hasAnyAuthority('WORK_WITH_OWN_TOPICS', 'WORK_WITH_OTHER_TOPICS')")
    public String redirectTopicsPageAfterSaving(HttpSession session,
                                                Authentication authentication,
                                                @Valid Topic topic,
                                                BindingResult bindingResult,
                                                @PathVariable(UrlPartConstants.SECTION_ID) String pathSectionId) {

        Integer sectionId = toNonNegativeInteger(pathSectionId);

        if (service.savingValidation(topic, bindingResult)) {
            session.setAttribute("object", topic);
            session.setAttribute("formSubmitButtonText", service.isNew(topic) ? "Создать тему" : "Сохранить");
            session.setAttribute("errorMessage", service.anyError(bindingResult));
            return "redirect:%s/%s".formatted(ControllerBaseUrlConstants.FOR_TOPICS_CONTROLLER, "create");
        }

        service.save(topic, extractCurrentUser(authentication), sectionService.findById(sectionId));
        return "redirect:%s".formatted(ControllerBaseUrlConstants.FOR_TOPICS_CONTROLLER);
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasAnyAuthority('WORK_WITH_OWN_TOPICS', 'WORK_WITH_OTHER_TOPICS')")
    public String returnTopicFormPageForEditing(HttpSession session, @PathVariable("id") String pathId) {

        Integer id = toNonNegativeInteger(pathId);

        session.setAttribute("object", service.findById(id));
        session.setAttribute("formSubmitButtonText", "Сохранить");

        return "redirect:%s/%s".formatted(ControllerBaseUrlConstants.FOR_TOPICS_CONTROLLER, "create");
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('WORK_WITH_OWN_TOPICS', 'WORK_WITH_OTHER_TOPICS')")
    public String redirectTopicsPageAfterDeleting(HttpSession session, @PathVariable("id") String pathId) {

        Integer id = toNonNegativeInteger(pathId);

        String msg = service.deletingValidation(service.findById(id));
        if (msg != null) {
            session.setAttribute("errorMessage", msg);
            return "redirect:%s".formatted(ControllerBaseUrlConstants.FOR_TOPICS_CONTROLLER);
        }

        service.deleteById(id);
        return "redirect:%s".formatted(ControllerBaseUrlConstants.FOR_TOPICS_CONTROLLER);
    }

    private List<Topic> sorted(TopicSortingOption sortingOption, Integer sectionId) {
        return sortingOption != null
                ? service.findAllBySectionIdSorted(sectionId, sortingOption)
                : service.findAllBySectionIdSorted(sectionId);
    }

    private List<Topic> searchedAndSorted(TopicSortingOption sortingOption, String searchedText, Integer sectionId) {
        return searchedText != null && !searchedText.isEmpty()
                ? service.search(sorted(sortingOption, sectionId), searchedText)
                : sorted(sortingOption, sectionId);
    }

}
