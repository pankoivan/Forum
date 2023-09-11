package org.forum.main.controllers.mvc;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.forum.auxiliary.sorting.enums.TopicSortingProperties;
import org.forum.auxiliary.sorting.options.TopicSortingOption;
import org.forum.main.controllers.mvc.common.ConvenientController;
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
@RequestMapping("/sections/{sectionId}/topics")
public class TopicsController extends ConvenientController {

    private final SectionService sectionService;

    private final TopicService service;

    @Autowired
    public TopicsController(SectionService sectionService, TopicService service) {
        this.sectionService = sectionService;
        this.service = service;
    }

    @GetMapping
    public String returnTopicsPage(Model model,
                                   Authentication authentication,
                                   @SessionAttribute(value = "topicSortingOption", required = false)
                                       TopicSortingOption sortingOption,
                                   @PathVariable("sectionId") Integer sectionId) {

        addForHeader(model, authentication, sectionService);
        add(model, "section", sectionService.findById(sectionId));
        add(model, "topics", sorted(sortingOption, sectionId));
        add(model, "page", "topics");

        add(model, "sortingObject", service.emptySortingOption());
        add(model, "properties", TopicSortingProperties.values());
        add(model, "directions", Sort.Direction.values());

        return "topics";
    }

    @GetMapping("/create")
    public String returnTopicFormPageForCreating(Model model,
                                                 Authentication authentication,
                                                 @PathVariable("sectionId") Integer sectionId) {

        addForHeader(model, authentication, sectionService);
        add(model, "object", service.empty());
        add(model, "sectionId", sectionId);
        add(model, "formSubmitButtonText", "Создать тему");
        return "topic-form";
    }

    @PostMapping("/inner/save")
    public String redirectTopicsPageAfterSaving(Model model,
                                                Authentication authentication,
                                                @Valid Topic topic,
                                                BindingResult bindingResult,
                                                @PathVariable("sectionId") Integer sectionId) {

        if (service.savingValidation(topic, bindingResult)) {
            addForHeader(model, authentication, sectionService);
            add(model, "object", topic);
            add(model, "sectionId", sectionId);
            add(model, "formSubmitButtonText", service.isNew(topic) ? "Создать тему" : "Сохранить");
            add(model, "error", service.extractAnySingleError(bindingResult));
            return "topic-form";
        }

        service.save(topic, authentication, sectionService.findById(sectionId));
        return "redirect:/sections/{sectionId}/topics";
    }

    @PostMapping("/inner/edit/{topicId}")
    public String returnTopicFormPageForEditing(Model model,
                                                Authentication authentication,
                                                @PathVariable("topicId") Integer id,
                                                @PathVariable("sectionId") Integer sectionId) {

        addForHeader(model, authentication, sectionService);
        add(model, "object", service.findById(id));
        add(model, "sectionId", sectionId);
        add(model, "formSubmitButtonText", "Сохранить");
        return "topic-form";
    }

    @PostMapping("/inner/delete/{topicId}")
    public String redirectTopicsPageAfterDeleting(Model model,
                                                  Authentication authentication,
                                                  @SessionAttribute(value = "topicSortingOption", required = false)
                                                      TopicSortingOption sortingOption,
                                                  @PathVariable("topicId") Integer id,
                                                  @PathVariable("sectionId") Integer sectionId) {

        String msg = service.deletingValidation(service.findById(id));
        if (msg != null) {
            addForHeader(model, authentication, sectionService);
            add(model, "section", sectionService.findById(sectionId));
            add(model, "topics", sorted(sortingOption, sectionId));
            add(model, "page", "topics");
            add(model, "error", msg);
            return "topics";
        }

        service.deleteById(id);
        return "redirect:/sections/{sectionId}/topics";
    }

    @PostMapping("/sort")
    public String redirectTopicsPageAfterSorting(HttpSession session, TopicSortingOption sortingOption) {
        session.setAttribute("topicSortingOption", sortingOption);
        return "redirect:/sections/{sectionId}/topics";
    }

    private List<Topic> sorted(TopicSortingOption sortingOption, Integer sectionId) {
        return sortingOption != null
                ? service.findAllBySectionIdSorted(sectionId, sortingOption)
                : service.findAllBySectionIdSortedByDefault(sectionId);
    }

}
