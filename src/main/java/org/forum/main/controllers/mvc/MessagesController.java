package org.forum.main.controllers.mvc;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.forum.auxiliary.sorting.options.MessageSortingOption;
import org.forum.auxiliary.sorting.enums.MessageSortingProperties;
import org.forum.main.controllers.mvc.common.ConvenientController;
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
                                     @SessionAttribute(value = "messageSortingOption", required = false)
                                         MessageSortingOption sortingOption,
                                     @PathVariable("sectionId") String pathSectionId,
                                     @PathVariable("topicId") String pathTopicId,
                                     @PathVariable("pageNumber") String pathPageNumber) {

        Integer sectionId = toNonNegativeInteger(pathSectionId);
        Integer topicId = toNonNegativeInteger(pathTopicId);
        Integer pageNumber = toNonNegativeInteger(pathPageNumber);

        addForHeader(model, authentication, sectionService);
        add(model, "sectionId", sectionId);
        add(model, "topicId", topicId);
        add(model, "topicName", topicService.findById(topicId).getName());
        add(model, "messages", sorted(sortingOption, topicId, pageNumber));
        add(model, "message", service.empty());
        add(model, "formSubmitButtonText", "Отправить сообщение");
        add(model, "pagesCount", service.pagesCount(service.findAllByTopicId(topicId)));
        add(model, "currentPage", pageNumber);
        add(model, "sortingObject", service.emptySortingOption());
        add(model, "properties", MessageSortingProperties.values());
        add(model, "directions", Sort.Direction.values());

        return "messages";
    }

    @PostMapping("/page{pageNumber}/inner/save")
    public String redirectMessagesPageAfterSaving(Model model,
                                                  Authentication authentication,
                                                  @Valid Message message,
                                                  BindingResult bindingResult,
                                                  @SessionAttribute(value = "messageSortingOption", required = false)
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
            add(model, "sectionId", sectionId);
            add(model, "topicId", topicId);
            add(model, "topicName", topicService.findById(topicId).getName());
            add(model, "messages", sorted(sortingOption, topicId, pageNumber));
            add(model, "message", message);
            add(model, "formSubmitButtonText", isNew ? "Отправить сообщение" : "Сохранить изменения");
            add(model, "pagesCount", service.pagesCount(service.findAllByTopicId(topicId)));
            add(model, "currentPage", pageNumber);
            add(model, "sortingObject", service.emptySortingOption());
            add(model, "properties", MessageSortingProperties.values());
            add(model, "directions", Sort.Direction.values());
            add(model, "formError", service.extractAnySingleError(bindingResult));

            return "messages";
        }

        service.save(message, authentication, topicService.findById(topicId));

        return isNew
                ? "redirect:/sections/{sectionId}/topics/{topicId}/messages/page" +
                        service.pagesCount(service.findAllByTopicId(topicId))
                : "redirect:/sections/{sectionId}/topics/{topicId}/messages/page{pageNumber}";
    }

    @PostMapping("/page{pageNumber}/inner/edit/{id}")
    public String returnMessagesPageForEditing(Model model,
                                               Authentication authentication,
                                               @SessionAttribute(value = "messageSortingOption", required = false)
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
        add(model, "sectionId", sectionId);
        add(model, "topicId", topicId);
        add(model, "topicName", topicService.findById(topicId).getName());
        add(model, "messages", sorted(sortingOption, topicId, pageNumber));
        add(model, "message", service.findById(id));
        add(model, "formSubmitButtonText", "Сохранить изменения");
        add(model, "pagesCount", service.pagesCount(service.findAllByTopicId(topicId)));
        add(model, "currentPage", pageNumber);
        add(model, "sortingObject", service.emptySortingOption());
        add(model, "properties", MessageSortingProperties.values());
        add(model, "directions", Sort.Direction.values());

        return "messages";
    }

    @PostMapping("/page{pageNumber}/inner/delete/{id}")
    public String redirectMessagesPageAfterDeleting(Model model,
                                                    Authentication authentication,
                                                    @SessionAttribute(value = "messageSortingOption", required = false)
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
            add(model, "sortingObject", service.emptySortingOption());
            add(model, "properties", MessageSortingProperties.values());
            add(model, "directions", Sort.Direction.values());
            add(model, "error", msg);

            return "messages";
        }

        service.deleteById(id);

        int newPagesCount = service.pagesCount(service.findAllByTopicId(topicId));

        return pageNumber.equals(pagesCount) && newPagesCount < pagesCount
                ? "redirect:/sections/{sectionId}/topics/{topicId}/messages/page" + newPagesCount
                : "redirect:/sections/{sectionId}/topics/{topicId}/messages/page{pageNumber}";
    }

    @PostMapping("/page{pageNumber}/sort")
    public String redirectCurrentPageAfterSorting(HttpSession session, MessageSortingOption sortingOption) {
        session.setAttribute("messageSortingOption", sortingOption);
        return "redirect:/sections/{sectionId}/topics/{topicId}/messages/page{pageNumber}";
    }

    private List<Message> sorted(MessageSortingOption sortingOption, Integer topicId, Integer pageNumber) {
        return sortingOption != null
                ? service.onPage(service.findAllByTopicIdSorted(topicId, sortingOption), pageNumber)
                : service.onPage(service.findAllByTopicIdSortedByDefault(topicId), pageNumber);
    }

}
