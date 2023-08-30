package org.forum.controllers.mvc;

import jakarta.validation.Valid;
import org.forum.controllers.mvc.common.ConvenientController;
import org.forum.entities.Message;
import org.forum.services.interfaces.MessageService;
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
@RequestMapping("/sections/{sectionId}/topics/{topicId}/messages")
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
        return "redirect:/sections/{sectionId}/topics/{topicId}/messages/page1";
    }

    @GetMapping("/page{pageNumber}")
    public String returnMessagesPage(Model model,
                                     Authentication authentication,
                                     @PathVariable("sectionId") Integer sectionId,
                                     @PathVariable("topicId") Integer topicId,
                                     @PathVariable("pageNumber") Integer pageNumber) {

        addForHeader(model, authentication, sectionService);
        add(model, "message", service.empty());
        add(model, "messages", service.findAllForPage(pageNumber));
        add(model, "section", sectionService.findById(sectionId));
        add(model, "topic", topicService.findById(topicId));
        add(model, "formSubmitButtonText", "Отправить сообщение");
        add(model, "pagesCount", service.pagesCount());
        return "messages";
    }

    @PostMapping("/inner/save")
    public String redirectMessagesPageAfterSaving(Model model,
                                                  Authentication authentication,
                                                  @Valid Message message,
                                                  BindingResult bindingResult,
                                                  @PathVariable("sectionId") Integer sectionId,
                                                  @PathVariable("topicId") Integer topicId) {

        if (service.savingValidation(message, bindingResult)) {
            addForHeader(model, authentication, sectionService);
            add(model, "message", message);
            add(model, "messages", service.findAllByTopicId(topicId));
            add(model, "section", sectionService.findById(sectionId));
            add(model, "topic", topicService.findById(topicId));
            add(model, "formSubmitButtonText", service.isNew(message)
                    ? "Отправить сообщение" : "Сохранить изменения");
            add(model, "formError", service.extractAnySingleError(bindingResult));
            return "messages";
        }

        service.save(message, authentication, topicService.findById(topicId));
        return "redirect:/sections/{sectionId}/topics/{topicId}/messages";
    }

    @PostMapping("/inner/edit/{id}")
    public String returnMessagesPageForEditing(Model model,
                                               Authentication authentication,
                                               @PathVariable("id") Long id,
                                               @PathVariable("sectionId") Integer sectionId,
                                               @PathVariable("topicId") Integer topicId) {

        addForHeader(model, authentication, sectionService);
        add(model, "message", service.findById(id));
        add(model, "messages", service.findAllByTopicId(topicId));
        add(model, "section", sectionService.findById(sectionId));
        add(model, "topic", topicService.findById(topicId));
        add(model, "formSubmitButtonText", "Сохранить изменения");
        return "messages";
    }

    @PostMapping("/inner/delete/{id}")
    public String redirectMessagesPageAfterDeleting(Model model,
                                                    Authentication authentication,
                                                    @PathVariable("id") Long id,
                                                    @PathVariable("sectionId") Integer sectionId,
                                                    @PathVariable("topicId") Integer topicId) {

        String msg = service.deletingValidation(service.findById(id));
        if (msg != null) {
            addForHeader(model, authentication, sectionService);
            add(model, "message", service.empty());
            add(model, "messages", service.findAllByTopicId(topicId));
            add(model, "section", sectionService.findById(sectionId));
            add(model, "topic", topicService.findById(topicId));
            add(model, "formSubmitButtonText", "Отправить сообщение");
            add(model, "error", msg);
            return "messages";
        }

        service.deleteById(id);
        return "redirect:/sections/{sectionId}/topics/{topicId}/messages";
    }

}
