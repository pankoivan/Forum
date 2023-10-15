package org.forum.main.controllers;

import jakarta.validation.Valid;
import org.forum.auxiliary.constants.ControllerBaseUrlConstants;
import org.forum.auxiliary.constants.SortingOptionNameConstants;
import org.forum.auxiliary.constants.UrlPartConstants;
import org.forum.auxiliary.sorting.enums.SectionSortingProperties;
import org.forum.auxiliary.sorting.enums.TopicSortingProperties;
import org.forum.auxiliary.sorting.options.SectionSortingOption;
import org.forum.main.controllers.common.ConvenientController;
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
@RequestMapping(ControllerBaseUrlConstants.FOR_SECTIONS_CONTROLLER)
public class SectionsController extends ConvenientController {

    private final SectionService service;

    @Autowired
    public SectionsController(SectionService service) {
        this.service = service;
    }

    @GetMapping
    public String redirectSectionsPageWithPagination() {
        return "redirect:%s/page1"
                .formatted(ControllerBaseUrlConstants.FOR_SECTIONS_CONTROLLER);
    }

    @GetMapping("/page{pageNumber}")
    public String returnSectionsPage(Model model,
                                     Authentication authentication,
                                     @SessionAttribute(value = SortingOptionNameConstants.FOR_SECTION_SORTING_OPTION,
                                             required = false)
                                         SectionSortingOption sortingOption,
                                     @PathVariable("pageNumber") String pathPageNumber) {

        Integer pageNumber = toNonNegativeInteger(pathPageNumber);

        addForHeader(model, authentication, service);

        add(model, "page", "sections");
        add(model, "sections", sorted(sortingOption, pageNumber));
        add(model, "pagesCount", service.pagesCount(service.findAll()));
        add(model, "currentPage", pageNumber);
        add(model, "paginationUrl", ControllerBaseUrlConstants.FOR_SECTIONS_CONTROLLER);
        add(model, "sortingObject", sortingOption == null ? service.emptySortingOption() : sortingOption);
        add(model, "properties", SectionSortingProperties.values());
        add(model, "directions", Sort.Direction.values());
        add(model, "sortingOptionName", SortingOptionNameConstants.FOR_SECTION_SORTING_OPTION);
        add(model, "sortingSubmitUrl", ControllerBaseUrlConstants.FOR_SORTING_CONTROLLER +
                UrlPartConstants.SECTIONS);
        add(model, "sortingSourcePageUrl", ControllerBaseUrlConstants.FOR_SECTIONS_CONTROLLER);

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

        return "redirect:%s"
                .formatted(ControllerBaseUrlConstants.FOR_SECTIONS_CONTROLLER);
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
                                                    @SessionAttribute(value = SortingOptionNameConstants.FOR_SECTION_SORTING_OPTION,
                                                            required = false)
                                                        SectionSortingOption sortingOption,
                                                    @PathVariable("id") String pathId) {

        Integer id = toNonNegativeInteger(pathId);

        String msg = service.deletingValidation(service.findById(id));
        if (msg != null) {

            addForHeader(model, authentication, service);

            add(model, "sections", sorted(sortingOption, 1));
            add(model, "page", "sections");
            add(model, "pagesCount", service.pagesCount(service.findAll()));
            add(model, "currentPage", 1);
            add(model, "paginationUrl", ControllerBaseUrlConstants.FOR_SECTIONS_CONTROLLER);
            add(model, "sortingObject", sortingOption == null ? service.emptySortingOption() : sortingOption);
            add(model, "properties", TopicSortingProperties.values());
            add(model, "directions", Sort.Direction.values());
            add(model, "sortingOptionName", SortingOptionNameConstants.FOR_SECTION_SORTING_OPTION);
            add(model, "sortingSubmitUrl", ControllerBaseUrlConstants.FOR_SORTING_CONTROLLER +
                    UrlPartConstants.SECTIONS);
            add(model, "sortingSourcePageUrl", ControllerBaseUrlConstants.FOR_SECTIONS_CONTROLLER);
            add(model, "error", msg);

            return "sections";
        }

        service.deleteById(id);

        return "redirect:%s"
                .formatted(ControllerBaseUrlConstants.FOR_SECTIONS_CONTROLLER);
    }

    private List<Section> sorted(SectionSortingOption sortingOption, Integer pageNumber) {
        return sortingOption != null
                ? service.onPage(service.findAllSorted(sortingOption), pageNumber)
                : service.onPage(service.findAllSortedByDefault(), pageNumber);
    }

}
