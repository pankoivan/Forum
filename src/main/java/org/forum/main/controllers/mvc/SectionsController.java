package org.forum.main.controllers.mvc;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.forum.auxiliary.sorting.enums.SectionSortingProperties;
import org.forum.auxiliary.sorting.options.SectionSortingOption;
import org.forum.main.controllers.mvc.common.ConvenientController;
import org.forum.main.entities.Section;
import org.forum.main.services.interfaces.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/sections")
public class SectionsController extends ConvenientController {

    private final SectionService service;

    @Autowired
    public SectionsController(SectionService service) {
        this.service = service;
    }

    @GetMapping
    public String returnSectionsPage(Model model,
                                     Authentication authentication,
                                     @SessionAttribute(value = "sectionSortingOption", required = false)
                                         SectionSortingOption sortingOption) {

        addForHeader(model, authentication, service);

        add(model, "sections", sorted(sortingOption));
        add(model, "page", "sections");

        add(model, "sortingObject", service.emptySortingOption());
        add(model, "properties", SectionSortingProperties.values());
        add(model, "directions", Sort.Direction.values());

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
                                                  @PathVariable("id") String pathId) {

        Integer id = toNonNegativeInteger(pathId);

        addForHeader(model, authentication, service);

        add(model, "object", service.findById(id));
        add(model, "formSubmitButtonText", "Сохранить");

        return "section-form";
    }

    @PostMapping("/inner/delete/{id}")
    public String redirectSectionsPageAfterDeleting(Model model,
                                                    Authentication authentication,
                                                    @SessionAttribute(value = "sectionSortingOption", required = false)
                                                        SectionSortingOption sortingOption,
                                                    @PathVariable("id") String pathId) {

        Integer id = toNonNegativeInteger(pathId);

        String msg = service.deletingValidation(service.findById(id));
        if (msg != null) {

            addForHeader(model, authentication, service);

            add(model, "sections", sorted(sortingOption));
            add(model, "page", "sections");

            add(model, "error", msg);

            return "sections";
        }

        service.deleteById(id);
        return "redirect:/sections";
    }

    @PostMapping("/sort")
    public String redirectSectionsPageAfterSorting(HttpSession session, SectionSortingOption sortingOption) {
        session.setAttribute("sectionSortingOption", sortingOption);
        return "redirect:/sections";
    }

    private List<Section> sorted(SectionSortingOption sortingOption) {
        return sortingOption != null
                ? service.findAllSorted(sortingOption)
                : service.findAllSortedByDefault();
    }

}
