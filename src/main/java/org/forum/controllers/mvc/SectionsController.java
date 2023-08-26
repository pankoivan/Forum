package org.forum.controllers.mvc;

import jakarta.validation.Valid;
import org.forum.controllers.mvc.interfaces.ConvenientController;
import org.forum.entities.Section;
import org.forum.services.interfaces.SectionService;
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
public class SectionsController implements ConvenientController {

    private final SectionService service;

    @Autowired
    public SectionsController(SectionService service) {
        this.service = service;
    }

    @GetMapping
    public String returnSectionsPage(Model model, Authentication authentication) {
        addCurrentUser(model, authentication);
        add(model, "sections", service.findAll());
        return "sections";
    }

    @GetMapping("/create")
    public String returnSectionFormPageForCreating(Model model, Authentication authentication) {
        addCurrentUser(model, authentication);
        add(model, "section", service.empty());
        add(model, "formSubmitButtonText", "Создать раздел");
        return "section-form";
    }

    @PostMapping("/inner/save")
    public String redirectSectionsPageAfterCreating(Model model,
                                                    Authentication authentication,
                                                    @Valid Section section,
                                                    BindingResult bindingResult) {

        if (service.savingValidation(section, bindingResult)) {
            addCurrentUser(model, authentication);
            add(model, "section", section);
            add(model, "error", service.extractAnySingleError(bindingResult));
            add(model, "formSubmitButtonText", service.isNew(section) ? "Создать раздел" : "Сохранить");
            return "section-form";
        }

        service.save(section);
        return "redirect:/sections";
    }

    @PostMapping("/inner/edit/{id}")
    public String returnSectionFormPageForEditing(Model model,
                                                  Authentication authentication,
                                                  @PathVariable("id") Integer id) {

        addCurrentUser(model, authentication);
        add(model, "section", service.findById(id));
        add(model, "formSubmitButtonText", "Сохранить");
        return "section-form";
    }

    @PostMapping("/inner/delete/{id}")
    public String redirectSectionsPageAfterDeleting(Model model,
                                                    Authentication authentication,
                                                    @PathVariable("id") Integer id) {

        String msg = service.deletingValidation(service.findById(id));
        if (msg != null) {
            addCurrentUser(model, authentication);
            add(model, "sections", service.findAll());
            add(model, "error", msg);
            return "sections";
        }

        service.deleteById(id);
        return "redirect:/sections";
    }

}
