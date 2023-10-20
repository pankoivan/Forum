package org.forum.main.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.forum.auxiliary.constants.CommonAttributeNameConstants;
import org.forum.auxiliary.constants.pagination.PaginationAttributeNameConstants;
import org.forum.auxiliary.constants.sorting.SortingAttributeNameConstants;
import org.forum.auxiliary.constants.url.ControllerBaseUrlConstants;
import org.forum.auxiliary.constants.sorting.SortingOptionNameConstants;
import org.forum.auxiliary.constants.url.UrlPartConstants;
import org.forum.auxiliary.sorting.enums.SectionSortingProperties;
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
        return "redirect:%s/%s1"
                .formatted(ControllerBaseUrlConstants.FOR_SECTIONS_CONTROLLER, UrlPartConstants.PAGE);
    }

    @GetMapping("/" + UrlPartConstants.PAGE_PAGE_NUMBER_PATTERN)
    public String returnSectionsPage(HttpServletRequest request,
                                     Model model,
                                     Authentication authentication,
                                     @SessionAttribute(value = SortingOptionNameConstants.FOR_SECTIONS_SORTING_OPTION,
                                             required = false)
                                         SectionSortingOption sortingOption,
                                     @PathVariable(UrlPartConstants.PAGE_NUMBER) String pathPageNumber) {

        Integer pageNumber = toNonNegativeInteger(pathPageNumber);

        List<Section> sections = sorted(sortingOption);

        addForHeader(model, authentication, service);
        add(model, "page", "sections");
        add(model, "sections", service.onPage(sections, pageNumber));
        add(model, "isEditDeleteButtonsEnabled", true);
        currentPage(model, request.getRequestURI());
        pagination(model, service.pagesCount(sections), pageNumber);
        sorting(model, sortingOption);

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
            add(model, "error", service.anyError(bindingResult));

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
    public String redirectSectionsPageAfterDeleting(HttpServletRequest request,
                                                    Model model,
                                                    Authentication authentication,
                                                    @SessionAttribute(value = SortingOptionNameConstants.FOR_SECTIONS_SORTING_OPTION,
                                                            required = false)
                                                        SectionSortingOption sortingOption,
                                                    @PathVariable("id") String pathId) {

        Integer id = toNonNegativeInteger(pathId);

        String msg = service.deletingValidation(service.findById(id));
        if (msg != null) {

            List<Section> sections = sorted(sortingOption);

            addForHeader(model, authentication, service);
            add(model, "page", "sections");
            add(model, "sections", service.onPage(sections, 1));
            add(model, "isEditDeleteButtonsEnabled", true);
            currentPage(model, request.getRequestURI());
            pagination(model, service.pagesCount(sections), 1);
            sorting(model, sortingOption);
            add(model, "error", msg);

            return "sections";
        }

        service.deleteById(id);

        return "redirect:%s"
                .formatted(ControllerBaseUrlConstants.FOR_SECTIONS_CONTROLLER);
    }

    private void currentPage(Model model, String currentUrl) {
        add(model, CommonAttributeNameConstants.SOURCE_PAGE_URL_WITH_PAGE, currentUrl);
        add(model, CommonAttributeNameConstants.SOURCE_PAGE_URL_WITHOUT_PAGE, removePage(currentUrl));
    }

    private void pagination(Model model, Integer pagesCount, Integer currentPage) {
        add(model, PaginationAttributeNameConstants.PAGES_COUNT, pagesCount);
        add(model, PaginationAttributeNameConstants.CURRENT_PAGE, currentPage);
    }

    private void sorting(Model model, SectionSortingOption sortingOption) {

        add(model, SortingAttributeNameConstants.SORTING_OBJECT,
                sortingOption == null ? service.emptySortingOption() : sortingOption);

        add(model, SortingAttributeNameConstants.SORTING_PROPERTIES,
                SectionSortingProperties.values());

        add(model, SortingAttributeNameConstants.SORTING_DIRECTIONS,
                Sort.Direction.values());

        add(model, SortingAttributeNameConstants.SORTING_OPTION_NAME,
                SortingOptionNameConstants.FOR_SECTIONS_SORTING_OPTION);

        add(model, SortingAttributeNameConstants.SORTING_SUBMIT_URL,
                concat(ControllerBaseUrlConstants.FOR_SORTING_CONTROLLER, UrlPartConstants.SECTIONS));
    }

    private List<Section> sorted(SectionSortingOption sortingOption) {
        return sortingOption != null
                ? service.findAllSorted(sortingOption)
                : service.findAllSorted();
    }

}
