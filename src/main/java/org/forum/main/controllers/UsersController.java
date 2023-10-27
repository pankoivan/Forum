package org.forum.main.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.forum.auxiliary.constants.CommonAttributeNameConstants;
import org.forum.auxiliary.constants.pagination.PaginationAttributeNameConstants;
import org.forum.auxiliary.constants.sorting.SortingAttributeNameConstants;
import org.forum.auxiliary.constants.url.ControllerBaseUrlConstants;
import org.forum.auxiliary.constants.sorting.SortingOptionNameConstants;
import org.forum.auxiliary.constants.url.UrlPartConstants;
import org.forum.auxiliary.exceptions.ControllerException;
import org.forum.auxiliary.sorting.properties.UserSortingProperties;
import org.forum.auxiliary.sorting.options.UserSortingOption;
import org.forum.main.controllers.common.ConvenientController;
import org.forum.main.entities.User;
import org.forum.main.services.interfaces.SectionService;
import org.forum.main.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(ControllerBaseUrlConstants.FOR_USERS_CONTROLLER)
@PreAuthorize("permitAll()")
public class UsersController extends ConvenientController {

    private static final String USUAL = "usual";

    private static final String MODERS = "moders";

    private static final String ADMINS = "admins";

    private final SectionService sectionService;

    private final UserService service;

    @Autowired
    public UsersController(SectionService sectionService, UserService service) {
        this.sectionService = sectionService;
        this.service = service;
    }

    @GetMapping
    public String redirectUsersPageWithPagination(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAllAttributes(request.getParameterMap());
        return "redirect:%s/%s1".formatted(request.getRequestURI(), UrlPartConstants.PAGE);
    }

    @GetMapping("/" + USUAL)
    public String redirectUsualUsersPageWithPagination(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAllAttributes(request.getParameterMap());
        return "redirect:%s/%s1".formatted(request.getRequestURI(), UrlPartConstants.PAGE);
    }

    @GetMapping("/" + MODERS)
    public String redirectModersPageWithPagination(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAllAttributes(request.getParameterMap());
        return "redirect:%s/%s1".formatted(request.getRequestURI(), UrlPartConstants.PAGE);
    }

    @GetMapping("/" + ADMINS)
    public String redirectAdminsPageWithPagination(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAllAttributes(request.getParameterMap());
        return "redirect:%s/%s1".formatted(request.getRequestURI(), UrlPartConstants.PAGE);
    }

    @GetMapping({
            "/{userUrlRole}/" + UrlPartConstants.PAGE_PAGE_NUMBER_PATTERN,
            "/" + UrlPartConstants.PAGE_PAGE_NUMBER_PATTERN
    })
    public String returnUsersPage(HttpServletRequest request,
                                  Model model,
                                  Authentication authentication,
                                  @SessionAttribute(value = SortingOptionNameConstants.FOR_USERS_SORTING_OPTION, required = false)
                                      UserSortingOption sortingOption,
                                  @RequestParam(value = CommonAttributeNameConstants.SEARCH, required = false) String searchedText,
                                  @PathVariable("userUrlRole") Optional<String> userUrlRole,
                                  @PathVariable(UrlPartConstants.PAGE_NUMBER) String pathPageNumber) {

        Integer pageNumber = toNonNegativeInteger(pathPageNumber);

        List<User> users = searchedAndSorted(sortingOption, searchedText, userUrlRole);

        addForHeader(model, authentication, sectionService);
        add(model, "page", userUrlRole.orElse("users"));
        add(model, "users", service.onPage(users, pageNumber));
        add(model, CommonAttributeNameConstants.SOURCE_PAGE_URL_WITH_PAGINATION, request.getRequestURI());
        add(model, CommonAttributeNameConstants.SOURCE_PAGE_URL_WITHOUT_PAGINATION, removePagination(request.getRequestURI()));
        add(model, CommonAttributeNameConstants.REQUEST_PARAMETERS, makeParametersString(request.getParameterMap()));
        add(model, PaginationAttributeNameConstants.PAGES_COUNT, service.pagesCount(users));
        add(model, PaginationAttributeNameConstants.CURRENT_PAGE, pageNumber);
        add(model, SortingAttributeNameConstants.SORTING_OBJECT, sortingOption == null ? service.emptySortingOption() : sortingOption);
        add(model, SortingAttributeNameConstants.SORTING_PROPERTIES, UserSortingProperties.values());
        add(model, SortingAttributeNameConstants.SORTING_DIRECTIONS, Sort.Direction.values());
        add(model, SortingAttributeNameConstants.SORTING_OPTION_NAME, SortingOptionNameConstants.FOR_USERS_SORTING_OPTION);
        add(model, SortingAttributeNameConstants.SORTING_SUBMIT_URL, concat(ControllerBaseUrlConstants.FOR_SORTING_CONTROLLER,
                UrlPartConstants.USERS));

        return "users";
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public String returnProfilePage(Model model,
                                    Authentication authentication,
                                    @PathVariable("id") String id) {

        Integer userId = toNonNegativeInteger(id);

        addForHeader(model, authentication, sectionService);
        add(model, "userForProfile", service.findById(userId));
        add(model, CommonAttributeNameConstants.IS_EDIT_DELETE_BUTTONS_ENABLED, false);
        add(model, CommonAttributeNameConstants.IS_LIKE_DISLIKE_BUTTONS_ENABLED, true);

        return "profile";
    }

    private String mySwitch(String userUrlRoleName) {
        return switch (userUrlRoleName) {
            case USUAL -> "ROLE_USER";
            case MODERS -> "ROLE_MODER";
            case ADMINS -> "ROLE_ADMIN";
            default -> throw new ControllerException("Unknown URL part: \"%s\"".formatted(userUrlRoleName));
        };
    }

    private List<User> sorted(UserSortingOption sortingOption, Optional<String> userUrlRole) {
        return sortingOption != null
                ? bySortingOption(sortingOption, userUrlRole)
                : byDefault(userUrlRole);
    }

    private List<User> bySortingOption(UserSortingOption sortingOption, Optional<String> userUrlRole) {
        return userUrlRole.isPresent()
                ? service.findAllByRoleNameSorted(mySwitch(userUrlRole.get()), sortingOption)
                : service.findAllSorted(sortingOption);
    }

    private List<User> byDefault(Optional<String> userUrlRole) {
        return userUrlRole.isPresent()
                ? service.findAllByRoleNameSorted(mySwitch(userUrlRole.get()))
                : service.findAllSorted();
    }

    private List<User> searchedAndSorted(UserSortingOption sortingOption, String searchedText, Optional<String> userUrlRole) {
        return searchedText != null && !searchedText.isEmpty()
                ? service.search(sorted(sortingOption, userUrlRole), searchedText)
                : sorted(sortingOption, userUrlRole);
    }

}
