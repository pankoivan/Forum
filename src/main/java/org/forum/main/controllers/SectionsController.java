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
import org.forum.auxiliary.sorting.enums.SectionSortingProperties;
import org.forum.auxiliary.sorting.options.SectionSortingOption;
import org.forum.auxiliary.utils.UrlUtils;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(ControllerBaseUrlConstants.FOR_SECTIONS_CONTROLLER)
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
                                     @SessionAttribute(value = SortingOptionNameConstants.FOR_SECTIONS_SORTING_OPTION,
                                             required = false)
                                         SectionSortingOption sortingOption,
                                     @SessionAttribute(value = "errorMessage", required = false) String errorMessage,
                                     @RequestParam(value = CommonAttributeNameConstants.SEARCH, required = false)
                                         String searchedText,
                                     @PathVariable(UrlPartConstants.PAGE_NUMBER) String pathPageNumber) {

        Integer pageNumber = toNonNegativeInteger(pathPageNumber);

        List<Section> sections = searchedAndSorted(sortingOption, searchedText);

        addForHeader(model, authentication, service);
        add(model, "page", "sections");
        add(model, "sections", service.onPage(sections, pageNumber));
        add(model, CommonAttributeNameConstants.IS_FOR_USER_CONTRIBUTIONS, false);
        add(model, CommonAttributeNameConstants.IS_EDIT_DELETE_BUTTONS_ENABLED, true);
        currentPage(model, request.getRequestURI());
        pagination(model, service.pagesCount(sections), pageNumber, request.getParameterMap());
        sorting(model, sortingOption);

        if (errorMessage != null) {
            add(model, "error", errorMessage);
            session.removeAttribute("errorMessage");
        }

        return "sections";
    }

    @GetMapping("/create")
    public String returnSectionFormPageForCreating(HttpSession session,
                                                   Model model,
                                                   Authentication authentication,
                                                   @SessionAttribute(value = "object", required = false) Section object,
                                                   @SessionAttribute(value = "formSubmitButtonText", required = false)
                                                       String formSubmitButtonText,
                                                   @SessionAttribute(value = "errorMessage", required = false)
                                                       String errorMessage) {

        addForHeader(model, authentication, service);
        add(model, "object", service.empty());
        add(model, "formSubmitButtonText", "Создать раздел");

        if (object != null) {
            add(model, "object", object);
            add(model, "formSubmitButtonText", formSubmitButtonText);
            if (errorMessage != null) {
                add(model, "error", errorMessage);
            }
            session.removeAttribute("object");
            session.removeAttribute("formSubmitButtonText");
            session.removeAttribute("errorMessage");
        }

        return "section-form";
    }

    @PostMapping("/save")
    public String redirectSectionsPageAfterSaving(HttpSession session,
                                                  Authentication authentication,
                                                  @Valid Section section,
                                                  BindingResult bindingResult) {

        if (service.savingValidation(section, bindingResult)) {
            session.setAttribute("object", section);
            session.setAttribute("formSubmitButtonText", service.isNew(section) ? "Создать раздел" : "Сохранить");
            session.setAttribute("errorMessage", service.anyError(bindingResult));
            return "redirect:%s/%s".formatted(ControllerBaseUrlConstants.FOR_SECTIONS_CONTROLLER, "create");
        }

        service.save(section, authentication);
        return "redirect:%s".formatted(ControllerBaseUrlConstants.FOR_SECTIONS_CONTROLLER);
    }

    @PostMapping("/edit/{id}")
    public String returnSectionFormPageForEditing(HttpSession session, @PathVariable("id") String pathId) {

        Integer id = toNonNegativeInteger(pathId);

        session.setAttribute("object", service.findById(id));
        session.setAttribute("formSubmitButtonText", "Сохранить");

        return "redirect:%s/%s".formatted(ControllerBaseUrlConstants.FOR_SECTIONS_CONTROLLER, "create");
    }

    @PostMapping("/delete/{id}")
    public String redirectSectionsPageAfterDeleting(HttpSession session, @PathVariable("id") String pathId) {

        Integer id = toNonNegativeInteger(pathId);

        String msg = service.deletingValidation(service.findById(id));
        if (msg != null) {
            session.setAttribute("errorMessage", msg);
            return "redirect:%s".formatted(ControllerBaseUrlConstants.FOR_SECTIONS_CONTROLLER);
        }

        service.deleteById(id);
        return "redirect:%s".formatted(ControllerBaseUrlConstants.FOR_SECTIONS_CONTROLLER);
    }

    private void currentPage(Model model, String currentUrl) {
        add(model, CommonAttributeNameConstants.SOURCE_PAGE_URL_WITH_PAGE, currentUrl);
        add(model, CommonAttributeNameConstants.SOURCE_PAGE_URL_WITHOUT_PAGE, removePage(currentUrl));
    }

    private void pagination(Model model, Integer pagesCount, Integer currentPage, Map<String, String[]> parameterMap) {
        add(model, PaginationAttributeNameConstants.PAGES_COUNT, pagesCount);
        add(model, PaginationAttributeNameConstants.CURRENT_PAGE, currentPage);
        add(model, CommonAttributeNameConstants.REQUEST_PARAMETERS, UrlUtils.makeParametersString(parameterMap));
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

    private List<Section> searchedAndSorted(SectionSortingOption sortingOption, String searchedText) {
        return searchedText != null && !searchedText.isEmpty()
                ? service.search(sorted(sortingOption), searchedText)
                : sorted(sortingOption);
    }

}
