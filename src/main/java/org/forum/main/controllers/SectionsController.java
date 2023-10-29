package org.forum.main.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.forum.auxiliary.constants.CommonAttributeNameConstants;
import org.forum.auxiliary.constants.pagination.PaginationAttributeNameConstants;
import org.forum.auxiliary.constants.sorting.SortingAttributeNameConstants;
import org.forum.auxiliary.constants.url.ControllerBaseUrlConstants;
import org.forum.auxiliary.constants.sorting.SortingOptionNameConstants;
import org.forum.auxiliary.constants.url.UrlPartConstants;
import org.forum.auxiliary.sorting.properties.SectionSortingProperties;
import org.forum.auxiliary.sorting.options.SectionSortingOption;
import org.forum.main.controllers.common.ConvenientController;
import org.forum.main.entities.Section;
import org.forum.main.services.interfaces.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping(ControllerBaseUrlConstants.FOR_SECTIONS_CONTROLLER)
@PreAuthorize("permitAll()")
public class SectionsController extends ConvenientController {

    private final SectionService service;

    @Autowired
    public SectionsController(SectionService service) {
        this.service = service;
    }

    @GetMapping
    public String redirectSectionsPageWithPagination(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAllAttributes(request.getParameterMap());
        return "redirect:%s/%s1".formatted(request.getRequestURI(), UrlPartConstants.PAGE);
    }

    @GetMapping("/" + UrlPartConstants.PAGE_PAGE_NUMBER_PATTERN)
    public String returnSectionsPage(HttpSession session,
                                     HttpServletRequest request,
                                     Model model,
                                     Authentication authentication,
                                     @SessionAttribute(value = SortingOptionNameConstants.FOR_SECTIONS_SORTING_OPTION, required = false)
                                         SectionSortingOption sortingOption,
                                     @SessionAttribute(value = "errorMessage", required = false) String errorMessage,
                                     @RequestParam(value = CommonAttributeNameConstants.SEARCH, required = false) String searchedText,
                                     @PathVariable(UrlPartConstants.PAGE_NUMBER) String pathPageNumber) {

        int pageNumber = toNonNegativeInteger(pathPageNumber);

        List<Section> sections = searchedAndSorted(sortingOption, searchedText);

        addForHeader(model, authentication, service);
        add(model, "sections", service.onPage(sections, pageNumber));
        add(model, CommonAttributeNameConstants.PAGE, "sections");
        add(model, CommonAttributeNameConstants.TITLE, "Разделы (стр. %s)".formatted(pageNumber));
        add(model, CommonAttributeNameConstants.IS_FOR_USER_CONTRIBUTIONS, false);
        add(model, CommonAttributeNameConstants.IS_EDIT_DELETE_BUTTONS_ENABLED, true);
        add(model, CommonAttributeNameConstants.SOURCE_PAGE_URL_WITH_PAGINATION, request.getRequestURI());
        add(model, CommonAttributeNameConstants.SOURCE_PAGE_URL_WITHOUT_PAGINATION, removePagination(request.getRequestURI()));
        add(model, CommonAttributeNameConstants.REQUEST_PARAMETERS, makeParametersString(request.getParameterMap()));
        add(model, PaginationAttributeNameConstants.PAGES_COUNT, service.pagesCount(sections));
        add(model, PaginationAttributeNameConstants.CURRENT_PAGE, pageNumber);
        add(model, SortingAttributeNameConstants.SORTING_OBJECT, sortingOption == null ? service.emptySortingOption() : sortingOption);
        add(model, SortingAttributeNameConstants.SORTING_PROPERTIES, SectionSortingProperties.values());
        add(model, SortingAttributeNameConstants.SORTING_DIRECTIONS, Sort.Direction.values());
        add(model, SortingAttributeNameConstants.SORTING_OPTION_NAME, SortingOptionNameConstants.FOR_SECTIONS_SORTING_OPTION);
        add(model, SortingAttributeNameConstants.SORTING_SUBMIT_URL, concat(ControllerBaseUrlConstants.FOR_SORTING_CONTROLLER,
                UrlPartConstants.SECTIONS));

        if (errorMessage != null) {
            add(model, "error", errorMessage);
            session.removeAttribute("errorMessage");
        }

        return "sections";
    }

    @GetMapping("/create")
    @PreAuthorize("hasAuthority('WORK_WITH_SECTIONS')")
    public String returnSectionFormPageForCreating(HttpSession session,
                                                   Model model,
                                                   Authentication authentication,
                                                   @SessionAttribute(value = "object", required = false) Section object,
                                                   @SessionAttribute(value = "formSubmitButtonText", required = false)
                                                       String formSubmitButtonText,
                                                   @SessionAttribute(value = "errorMessage", required = false) String errorMessage) {

        addForHeader(model, authentication, service);
        add(model, "object", service.empty());
        add(model, "formSubmitButtonText", "Создать раздел");

        if (object != null) {
            add(model, "object", object);
            add(model, "formSubmitButtonText", formSubmitButtonText);
            session.removeAttribute("object");
            session.removeAttribute("formSubmitButtonText");
        }
        if (errorMessage != null) {
            add(model, "error", errorMessage);
            session.removeAttribute("errorMessage");
        }

        return "section-form";
    }

    @PostMapping("/save")
    @PreAuthorize("hasAuthority('WORK_WITH_SECTIONS')")
    public String redirectSectionsPageAfterSaving(HttpSession session,
                                                  RedirectAttributes redirectAttributes,
                                                  Authentication authentication,
                                                  @Valid Section section,
                                                  BindingResult bindingResult,
                                                  @RequestParam(value = "pageNumber", required = false) String pageNumber) {

        boolean isNew = service.isNew(section);

        if (service.savingValidation(section, bindingResult)) {
            session.setAttribute("object", section);
            session.setAttribute("formSubmitButtonText", service.isNew(section) ? "Создать раздел" : "Сохранить");
            session.setAttribute("errorMessage", service.anyError(bindingResult));
            redirectAttributes.addAttribute("pageNumber", pageNumber);
            return "redirect:%s/%s".formatted(ControllerBaseUrlConstants.FOR_SECTIONS_CONTROLLER, "create");
        }

        service.save(section, extractCurrentUser(authentication));
        return "redirect:%s/%s%s".formatted(
                ControllerBaseUrlConstants.FOR_SECTIONS_CONTROLLER,
                UrlPartConstants.PAGE,
                isNew ? service.pagesCount(service.findAll()) : pageNumber
        );
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasAuthority('WORK_WITH_SECTIONS')")
    public String returnSectionFormPageForEditing(HttpSession session,
                                                  RedirectAttributes redirectAttributes,
                                                  @RequestParam(value = "pageNumber", required = false) String pageNumber,
                                                  @PathVariable("id") String pathId) {

        int id = toNonNegativeInteger(pathId);

        session.setAttribute("object", service.findById(id));
        session.setAttribute("formSubmitButtonText", "Сохранить");

        redirectAttributes.addAttribute("pageNumber", pageNumber);
        return "redirect:%s/%s".formatted(ControllerBaseUrlConstants.FOR_SECTIONS_CONTROLLER, "create");
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('WORK_WITH_SECTIONS')")
    public String redirectSectionsPageAfterDeleting(HttpSession session,
                                                    @RequestParam(value = "pageNumber", required = false) String pathPageNumber,
                                                    @PathVariable("id") String pathId) {

        int id = toNonNegativeInteger(pathId);
        int pageNumber = toNonNegativeInteger(pathPageNumber);
        int oldPagesCount = service.pagesCount(service.findAll());

        String msg = service.deletingValidation(service.findById(id));
        if (msg != null) {
            session.setAttribute("errorMessage", msg);
            return "redirect:%s/%s%s".formatted(
                    ControllerBaseUrlConstants.FOR_SECTIONS_CONTROLLER,
                    UrlPartConstants.PAGE,
                    pageNumber
            );
        }

        service.deleteById(id);
        int newPagesCount = service.pagesCount(service.findAll());
        return "redirect:%s/%s%s".formatted(
                ControllerBaseUrlConstants.FOR_SECTIONS_CONTROLLER,
                UrlPartConstants.PAGE,
                pageNumber == oldPagesCount && newPagesCount < oldPagesCount ? newPagesCount : pageNumber
        );
    }

    private List<Section> sorted(SectionSortingOption sortingOption) {
        return sortingOption != null
                ? service.findAllSorted(sortingOption)
                : service.findAllSorted();
    }

    private List<Section> searchedAndSorted(SectionSortingOption sortingOption, String searchedText) {
        return searchedText != null && !searchedText.isEmpty()
                ? service.search(sorted(sortingOption), searchedText)
                : sorted(sortingOption);
    }

}
