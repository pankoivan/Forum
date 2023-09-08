package org.forum.main.controllers.mvc;

import jakarta.validation.Valid;
import org.forum.auxiliary.constants.DefaultSortingOptionConstants;
import org.forum.auxiliary.sorting.SortingOption;
import org.forum.auxiliary.sorting.enums.SectionSortingProperties;
import org.forum.main.controllers.mvc.common.ConvenientController;
import org.forum.main.entities.Section;
import org.forum.main.services.interfaces.SectionService;
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
@RequestMapping("/sections")
@SuppressWarnings("unchecked")
public class SectionsController extends ConvenientController {

    private final SectionService service;

    @Autowired
    public SectionsController(SectionService service) {
        this.service = service;
    }

    @GetMapping
    public String returnSectionsPage(Model model, Authentication authentication) {
        addForHeader(model, authentication, service);
        add(model, "sections", service.findAllSorted(
                (SortingOption<SectionSortingProperties>) DefaultSortingOptionConstants.FOR_SECTIONS.getValue()));
        add(model, "page", "sections");
        return "sections";
    }

    @GetMapping("/create")
    public String returnSectionFormPageForCreating(Model model, Authentication authentication) {
        addForHeader(model, authentication, service);
        add(model, "object", service.empty());
        add(model, "formSubmitButtonText", "Создать раздел");
        return "section-form";
    }

    @PostMapping("/inner/save")
    public String redirectSectionsPageAfterSaving(Model model,
                                                  Authentication authentication,
                                                  @Valid Section section,
                                                  BindingResult bindingResult) {

        if (service.savingValidation(section, bindingResult)) {
            addForHeader(model, authentication, service);
            add(model, "object", section);
            add(model, "formSubmitButtonText", service.isNew(section) ? "Создать раздел" : "Сохранить");
            add(model, "error", service.extractAnySingleError(bindingResult));
            return "section-form";
        }

        service.save(section, authentication);
        return "redirect:/sections";
    }

    @PostMapping("/inner/edit/{id}")
    public String returnSectionFormPageForEditing(Model model,
                                                  Authentication authentication,
                                                  @PathVariable("id") Integer id) {

        addForHeader(model, authentication, service);
        add(model, "object", service.findById(id));
        add(model, "formSubmitButtonText", "Сохранить");
        return "section-form";
    }

    @PostMapping("/inner/delete/{id}")
    public String redirectSectionsPageAfterDeleting(Model model,
                                                    Authentication authentication,
                                                    @PathVariable("id") Integer id) {

        String msg = service.deletingValidation(service.findById(id));
        if (msg != null) {
            addForHeader(model, authentication, service);
            add(model, "sections", service.findAll());
            add(model, "page", "sections");
            add(model, "error", msg);
            return "sections";
        }

        service.deleteById(id);
        return "redirect:/sections";
    }

}
