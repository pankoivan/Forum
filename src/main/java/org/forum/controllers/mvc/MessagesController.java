package org.forum.controllers.mvc;

import org.forum.controllers.mvc.common.ConvenientController;
import org.forum.services.interfaces.MessageService;
import org.forum.services.interfaces.SectionService;
import org.forum.services.interfaces.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public String returnMessagesPage(Model model,
                                     Authentication authentication,
                                     @PathVariable("sectionId") Integer sectionId,
                                     @PathVariable("topicId") Integer topicId) {

        addForHeader(model, authentication, sectionService);
        add(model, "message", service.empty());
        add(model, "section", sectionService.findById(sectionId));
        add(model, "topic", topicService.findById(topicId));
        add(model,"messages", service.findAll());
        add(model, "formSubmitButtonText", "Отправить сообщение");
        return "messages";

    }

}
