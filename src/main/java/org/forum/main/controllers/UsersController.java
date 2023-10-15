package org.forum.main.controllers;

import org.forum.auxiliary.constants.ControllerBaseUrlConstants;
import org.forum.auxiliary.constants.SortingOptionNameConstants;
import org.forum.auxiliary.constants.UrlPartConstants;
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
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping(ControllerBaseUrlConstants.FOR_USERS_CONTROLLER)
public class UsersController extends ConvenientController {

    private final SectionService sectionService;

    private final UserService service;

    @Autowired
    public UsersController(SectionService sectionService, UserService service) {
        this.sectionService = sectionService;
        this.service = service;
    }

    @GetMapping
    public String redirectUsersPageWithPagination() {
        return "redirect:%s/page1"
                .formatted(ControllerBaseUrlConstants.FOR_USERS_CONTROLLER);
    }

    @GetMapping(UrlPartConstants.USUAL_USERS)
    public String redirectUsualUsersPageWithPagination() {
        return "redirect:%s%s/page1"
                .formatted(ControllerBaseUrlConstants.FOR_USERS_CONTROLLER, UrlPartConstants.USUAL_USERS);
    }

    @GetMapping(UrlPartConstants.MODER_USERS)
    public String redirectModersPageWithPagination() {
        return "redirect:%s%s/page1"
                .formatted(ControllerBaseUrlConstants.FOR_USERS_CONTROLLER, UrlPartConstants.MODER_USERS);
    }

    @GetMapping(UrlPartConstants.ADMIN_USERS)
    public String redirectAdminsPageWithPagination() {
        return "redirect:%s%s/page1"
                .formatted(ControllerBaseUrlConstants.FOR_USERS_CONTROLLER, UrlPartConstants.ADMIN_USERS);
    }

    @GetMapping({"/{userUrlRole}/page{pageNumber}", "/page{pageNumber}"})
    public String returnUsersPage(Model model,
                                  Authentication authentication,
                                  @SessionAttribute(value = SortingOptionNameConstants.FOR_USER_SORTING_OPTION,
                                          required = false)
                                      UserSortingOption sortingOption,
                                  @PathVariable(value = "userUrlRole") Optional<String> userUrlRole,
                                  @PathVariable("pageNumber") String pathPageNumber) {

        Integer pageNumber = toNonNegativeInteger(pathPageNumber);

        boolean isUserUrlRolePresent = userUrlRole.isPresent();

        addForHeader(model, authentication, sectionService);

        add(model, "users", sorted(sortingOption, userUrlRole, pageNumber));
        add(model, "page", isUserUrlRolePresent ? userUrlRole.get() : "users");
        add(model, "pagesCount", service.pagesCount(service.findAll()));
        add(model, "currentPage", pageNumber);
        add(model, "paginationUrl", ControllerBaseUrlConstants.FOR_USERS_CONTROLLER +
                (isUserUrlRolePresent ? ("/" + userUrlRole.get()) : ""));
        add(model, "sortingObject", sortingOption == null ? service.emptySortingOption() : sortingOption);
        add(model, "properties", UserSortingProperties.values());
        add(model, "directions", Sort.Direction.values());
        add(model, "sortingOptionName", SortingOptionNameConstants.FOR_USER_SORTING_OPTION);
        add(model, "sortingSubmitUrl", ControllerBaseUrlConstants.FOR_SORTING_CONTROLLER +
                UrlPartConstants.USERS);
        add(model, "sortingSourcePageUrl", ControllerBaseUrlConstants.FOR_USERS_CONTROLLER +
                (isUserUrlRolePresent ? ("/" + userUrlRole.get()) : ""));

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

    private String mySwitch(String roleUrlName) {
        return switch (addFirstSlash(roleUrlName)) {
            case UrlPartConstants.USUAL_USERS -> "ROLE_USER";
            case UrlPartConstants.MODER_USERS -> "ROLE_MODER";
            case UrlPartConstants.ADMIN_USERS -> "ROLE_ADMIN";
            default -> throw new ControllerException("Unknown URL part: \"%s\""
                    .formatted(roleUrlName));
        };
    }

    private List<User> sorted(UserSortingOption sortingOption, Optional<String> roleUrlName, Integer pageNumber) {
        return sortingOption != null
                ? service.onPage(bySortingOption(sortingOption, roleUrlName), pageNumber)
                : service.onPage(byDefault(roleUrlName), pageNumber);
    }

    private List<User> bySortingOption(UserSortingOption sortingOption, Optional<String> roleUrlName) {
        return roleUrlName.isPresent()
                ? service.findAllByRoleNameSorted(mySwitch(roleUrlName.get()), sortingOption)
                : service.findAllSorted(sortingOption);
    }

    private List<User> byDefault(Optional<String> roleUrlName) {
        return roleUrlName.isPresent()
                ? service.findAllByRoleNameSortedByDefault(mySwitch(roleUrlName.get()))
                : service.findAllSortedByDefault();
    }

}
