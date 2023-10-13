package org.forum.main.controllers;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.forum.auxiliary.constants.ControllerBaseUrlConstants;
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
        return "redirect:%s/page1"
                .formatted(ControllerBaseUrlConstants.FOR_TOPICS_CONTROLLER);
    }

    @GetMapping("/page{pageNumber}")
    public String returnTopicsPage(Model model,
                                   Authentication authentication,
                                   @SessionAttribute(value = "topicSortingOption", required = false)
                                       TopicSortingOption sortingOption,
                                   @PathVariable("sectionId") String pathSectionId,
                                   @PathVariable("pageNumber") String pathPageNumber) {

        Integer sectionId = toNonNegativeInteger(pathSectionId);
        Integer pageNumber = toNonNegativeInteger(pathPageNumber);

        addForHeader(model, authentication, sectionService);
        add(model, "sectionId", sectionId);
        add(model, "sectionName", sectionService.findById(sectionId).getName());
        add(model, "topics", sorted(sortingOption, sectionId, pageNumber));
        add(model, "page", "topics");
        add(model, "pagesCount", service.pagesCount(service.findAllBySectionId(sectionId)));
        add(model, "currentPage", pageNumber);
        add(model, "sortingObject", service.emptySortingOption());
        add(model, "properties", TopicSortingProperties.values());
        add(model, "directions", Sort.Direction.values());

        return "topics";
    }

    @GetMapping("/create")
    public String returnTopicFormPageForCreating(Model model,
                                                 Authentication authentication,
                                                 @PathVariable("sectionId") String pathSectionId) {

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
                                                @PathVariable("sectionId") String pathSectionId) {

        Integer sectionId = toNonNegativeInteger(pathSectionId);

        if (service.savingValidation(topic, bindingResult)) {

            addForHeader(model, authentication, sectionService);
            add(model, "sectionId", sectionId);
            add(model, "object", topic);
            add(model, "formSubmitButtonText", service.isNew(topic) ? "Создать тему" : "Сохранить");
            add(model, "error", service.extractAnySingleError(bindingResult));

            return "topic-form";
        }

        service.save(topic, authentication, sectionService.findById(sectionId));

        return "redirect:%s"
                .formatted(ControllerBaseUrlConstants.FOR_TOPICS_CONTROLLER);
    }

    @PostMapping("/inner/edit/{topicId}")
    public String returnTopicFormPageForEditing(Model model,
                                                Authentication authentication,
                                                @PathVariable("topicId") String pathId,
                                                @PathVariable("sectionId") String pathSectionId) {

        Integer id = toNonNegativeInteger(pathId);
        Integer sectionId = toNonNegativeInteger(pathSectionId);

        addForHeader(model, authentication, sectionService);
        add(model, "sectionId", sectionId);
        add(model, "object", service.findById(id));
        add(model, "formSubmitButtonText", "Сохранить");

        return "topic-form";
    }

    @PostMapping("/inner/delete/{topicId}")
    public String redirectTopicsPageAfterDeleting(Model model,
                                                  Authentication authentication,
                                                  @SessionAttribute(value = "topicSortingOption", required = false)
                                                      TopicSortingOption sortingOption,
                                                  @PathVariable("topicId") String pathId,
                                                  @PathVariable("sectionId") String pathSectionId) {

        Integer id = toNonNegativeInteger(pathId);
        Integer sectionId = toNonNegativeInteger(pathSectionId);

        String msg = service.deletingValidation(service.findById(id));
        if (msg != null) {

            addForHeader(model, authentication, sectionService);
            add(model, "sectionId", sectionId);
            add(model, "sectionName", sectionService.findById(sectionId).getName());
            add(model, "topics", sorted(sortingOption, sectionId, 1));
            add(model, "page", "topics");
            add(model, "pagesCount", service.pagesCount(service.findAllBySectionId(sectionId)));
            add(model, "currentPage", 1);
            add(model, "sortingObject", service.emptySortingOption());
            add(model, "properties", TopicSortingProperties.values());
            add(model, "directions", Sort.Direction.values());
            add(model, "error", msg);

            return "topics";
        }

        service.deleteById(id);

        return "redirect:%s"
                .formatted(ControllerBaseUrlConstants.FOR_TOPICS_CONTROLLER);
    }

    /*@PostMapping("/sort")
    public String redirectTopicsPageAfterSorting(HttpSession session, TopicSortingOption sortingOption) {
        session.setAttribute("topicSortingOption", sortingOption);
        return "redirect:%s"
                .formatted(ControllerBaseUrlConstants.FOR_TOPICS_CONTROLLER);
    }*/

    private List<Topic> sorted(TopicSortingOption sortingOption, Integer sectionId, Integer pageNumber) {
        return sortingOption != null
                ? service.onPage(service.findAllBySectionIdSorted(sectionId, sortingOption), pageNumber)
                : service.onPage(service.findAllBySectionIdSortedByDefault(sectionId), pageNumber);
    }

}
