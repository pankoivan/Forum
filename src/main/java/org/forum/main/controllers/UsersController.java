package org.forum.main.controllers;

import org.forum.auxiliary.constants.url.ControllerBaseUrlConstants;
import org.forum.auxiliary.constants.SortingOptionNameConstants;
import org.forum.auxiliary.constants.url.UrlPartConstants;
import org.forum.auxiliary.exceptions.ControllerException;
import org.forum.auxiliary.sorting.enums.UserSortingProperties;
import org.forum.auxiliary.sorting.options.UserSortingOption;
import org.forum.main.controllers.common.ConvenientController;
import org.forum.main.entities.User;
import org.forum.main.services.interfaces.SectionService;
import org.forum.main.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(ControllerBaseUrlConstants.FOR_USERS_CONTROLLER)
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
    public String redirectUsersPageWithPagination() {
        return "redirect:%s/%s1"
                .formatted(ControllerBaseUrlConstants.FOR_USERS_CONTROLLER, UrlPartConstants.PAGE);
    }

    @GetMapping("/" + USUAL)
    public String redirectUsualUsersPageWithPagination() {
        return "redirect:%s/%s/%s1"
                .formatted(ControllerBaseUrlConstants.FOR_USERS_CONTROLLER, USUAL, UrlPartConstants.PAGE);
    }

    @GetMapping("/" + MODERS)
    public String redirectModersPageWithPagination() {
        return "redirect:%s/%s/%s1"
                .formatted(ControllerBaseUrlConstants.FOR_USERS_CONTROLLER, MODERS, UrlPartConstants.PAGE);
    }

    @GetMapping("/" + ADMINS)
    public String redirectAdminsPageWithPagination() {
        return "redirect:%s/%s/%s1"
                .formatted(ControllerBaseUrlConstants.FOR_USERS_CONTROLLER, ADMINS, UrlPartConstants.PAGE);
    }

    @GetMapping({
            "/{userUrlRole}/" + UrlPartConstants.PAGE_PAGE_NUMBER_PATTERN,
            "/" + UrlPartConstants.PAGE_PAGE_NUMBER_PATTERN
    })
    public String returnUsersPage(Model model,
                                  Authentication authentication,
                                  @SessionAttribute(value = SortingOptionNameConstants.FOR_USER_SORTING_OPTION,
                                          required = false)
                                      UserSortingOption sortingOption,
                                  @PathVariable("userUrlRole") Optional<String> userUrlRole,
                                  @PathVariable(UrlPartConstants.PAGE_NUMBER) String pathPageNumber) {

        Integer pageNumber = toNonNegativeInteger(pathPageNumber);

        List<User> users = sorted(sortingOption, userUrlRole);

        addForHeader(model, authentication, sectionService);
        add(model, "page", userUrlRole.orElse("users"));
        add(model, "users", service.onPage(users, pageNumber));
        add(model, "pagesCount", service.pagesCount(users));
        add(model, "currentPage", pageNumber);
        add(model, "paginationUrl", ControllerBaseUrlConstants.FOR_USERS_CONTROLLER +
                addStartSlash(userUrlRole.orElse("")));
        add(model, "sortingObject", sortingOption == null ? service.emptySortingOption() : sortingOption);
        add(model, "properties", UserSortingProperties.values());
        add(model, "directions", Sort.Direction.values());
        add(model, "sortingOptionName", SortingOptionNameConstants.FOR_USER_SORTING_OPTION);
        add(model, "sortingSubmitUrl", ControllerBaseUrlConstants.FOR_SORTING_CONTROLLER +
                addStartSlash(UrlPartConstants.USERS));
        add(model, "sortingSourcePageUrl", ControllerBaseUrlConstants.FOR_USERS_CONTROLLER +
                (userUrlRole.isPresent() ? addStartSlash(userUrlRole.get()) : ""));

        return "users";
    }

    @GetMapping("/{id}")
    public String returnProfilePage(Model model,
                                    Authentication authentication,
                                    @PathVariable("id") String id) {

        Integer userId = toNonNegativeInteger(id);
        addForHeader(model, authentication, sectionService);
        add(model, "userForProfile", service.findById(userId));

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
                ? service.findAllByRoleNameSortedByDefault(mySwitch(userUrlRole.get()))
                : service.findAllSortedByDefault();
    }

}
