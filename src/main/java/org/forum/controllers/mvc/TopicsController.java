package org.forum.controllers.mvc;

import jakarta.validation.Valid;
import org.forum.controllers.mvc.common.ConvenientController;
import org.forum.entities.Topic;
import org.forum.services.interfaces.SectionService;
import org.forum.services.interfaces.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
                                   @PathVariable("sectionId") Integer sectionId) {

        addForHeader(model, authentication, sectionService);
        add(model, "section", sectionService.findById(sectionId));
        add(model, "topics", service.findAllBySectionId(sectionId));
        add(model, "page", "topics");
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
                                                  @PathVariable("topicId") Integer id,
                                                  @PathVariable("sectionId") Integer sectionId) {

        String msg = service.deletingValidation(service.findById(id));
        if (msg != null) {
            addForHeader(model, authentication, sectionService);
            add(model, "section", sectionService.findById(sectionId));
            add(model, "topics", service.findAllBySectionId(sectionId));
            add(model, "page", "topics");
            add(model, "error", msg);
            return "topics";
        }

        service.deleteById(id);
        return "redirect:/sections/{sectionId}/topics";
    }

}
