package org.forum.main.controllers;

import jakarta.servlet.http.HttpSession;
import org.forum.auxiliary.constants.ControllerBaseUrlConstants;
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

    @GetMapping("/page{pageNumber}")
    public String returnUsersPage(Model model,
                                  Authentication authentication,
                                  @SessionAttribute(value = "userSortingOption", required = false)
                                      UserSortingOption sortingOption,
                                  @PathVariable("pageNumber") String pathPageNumber) {

        Integer pageNumber = toNonNegativeInteger(pathPageNumber);

        addForHeader(model, authentication, sectionService);
        add(model, "users", sorted(sortingOption, pageNumber));
        add(model, "page", "users");
        add(model, "pagesCount", service.pagesCount(service.findAll()));
        add(model, "currentPage", pageNumber);
        add(model, "urlPartForPagination", "/users");
        add(model, "submitUrl", "/users/page%d/sort".formatted(pageNumber));
        add(model, "sortingObject", sortingOption == null ? service.emptySortingOption() : sortingOption);
        add(model, "properties", UserSortingProperties.values());
        add(model, "directions", Sort.Direction.values());

        return "users";
    }

    @GetMapping("/usual")
    public String redirectUsualUsersPageWithPagination() {
        return "redirect:%s/usual/page1"
                .formatted(ControllerBaseUrlConstants.FOR_USERS_CONTROLLER);
    }

    @GetMapping("/usual/page{pageNumber}")
    public String returnUsualUsersPage(Model model,
                                       Authentication authentication,
                                       @SessionAttribute(value = "userSortingOption", required = false)
                                           UserSortingOption sortingOption,
                                       @PathVariable("pageNumber") String pathPageNumber) {

        Integer pageNumber = toNonNegativeInteger(pathPageNumber);

        addForHeader(model, authentication, sectionService);
        add(model, "users", sorted(sortingOption, "ROLE_USER", pageNumber));
        add(model, "page", "usual");
        add(model, "pagesCount", service.pagesCount(service.findAll()));
        add(model, "currentPage", pageNumber);
        add(model, "urlPartForPagination", "/users/usual");
        add(model, "submitUrl", "/users/usual/page%d/sort".formatted(pageNumber));
        add(model, "sortingObject", sortingOption == null ? service.emptySortingOption() : sortingOption);
        add(model, "properties", UserSortingProperties.values());
        add(model, "directions", Sort.Direction.values());

        return "users";
    }

    @GetMapping("/moders")
    public String redirectModersPageWithPagination() {
        return "redirect:%s/moders/page1"
                .formatted(ControllerBaseUrlConstants.FOR_USERS_CONTROLLER);
    }

    @GetMapping("/moders/page{pageNumber}")
    public String returnModersPage(Model model,
                                   Authentication authentication,
                                   @SessionAttribute(value = "userSortingOption", required = false)
                                       UserSortingOption sortingOption,
                                   @PathVariable("pageNumber") String pathPageNumber) {

        Integer pageNumber = toNonNegativeInteger(pathPageNumber);

        addForHeader(model, authentication, sectionService);
        add(model, "users", sorted(sortingOption, "ROLE_MODER", pageNumber));
        add(model, "page", "moders");
        add(model, "pagesCount", service.pagesCount(service.findAll()));
        add(model, "currentPage", pageNumber);
        add(model, "urlPartForPagination", "/users/moders");
        add(model, "submitUrl", "/users/moders/page%d/sort".formatted(pageNumber));
        add(model, "sortingObject", sortingOption == null ? service.emptySortingOption() : sortingOption);
        add(model, "properties", UserSortingProperties.values());
        add(model, "directions", Sort.Direction.values());

        return "users";
    }

    @GetMapping("/admins")
    public String redirectAdminsPageWithPagination() {
        return "redirect:%s/admins/page1"
                .formatted(ControllerBaseUrlConstants.FOR_USERS_CONTROLLER);
    }

    @GetMapping("/admins/page{pageNumber}")
    public String returnAdminsPage(Model model,
                                   Authentication authentication,
                                   @SessionAttribute(value = "userSortingOption", required = false)
                                       UserSortingOption sortingOption,
                                   @PathVariable("pageNumber") String pathPageNumber) {

        Integer pageNumber = toNonNegativeInteger(pathPageNumber);

        addForHeader(model, authentication, sectionService);
        add(model, "users", sorted(sortingOption,"ROLE_ADMIN", pageNumber));
        add(model, "page", "admins");
        add(model, "pagesCount", service.pagesCount(service.findAll()));
        add(model, "currentPage", pageNumber);
        add(model, "urlPartForPagination", "/users/admins");
        add(model, "submitUrl", "/users/admins/page%d/sort".formatted(pageNumber));
        add(model, "sortingObject", sortingOption == null ? service.emptySortingOption() : sortingOption);
        add(model, "properties", UserSortingProperties.values());
        add(model, "directions", Sort.Direction.values());

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

    /*@PostMapping("/page{pageNumber}/sort")
    public String redirectCurrentUsersPageAfterSorting(HttpSession session, UserSortingOption sortingOption) {
        session.setAttribute("userSortingOption", sortingOption);
        return "redirect:%s/page{pageNumber}"
                .formatted(ControllerBaseUrlConstants.FOR_USERS_CONTROLLER);
    }

    @PostMapping("/usual/page{pageNumber}/sort")
    public String redirectCurrentUsualUsersPageAfterSorting(HttpSession session, UserSortingOption sortingOption) {
        session.setAttribute("userSortingOption", sortingOption);
        return "redirect:%s/usual/page{pageNumber}"
                .formatted(ControllerBaseUrlConstants.FOR_USERS_CONTROLLER);
    }

    @PostMapping("/moders/page{pageNumber}/sort")
    public String redirectCurrentModersPageAfterSorting(HttpSession session, UserSortingOption sortingOption) {
        session.setAttribute("userSortingOption", sortingOption);
        return "redirect:%s/moders/page{pageNumber}"
                .formatted(ControllerBaseUrlConstants.FOR_USERS_CONTROLLER);
    }

    @PostMapping("/admins/page{pageNumber}/sort")
    public String redirectCurrentAdminsPageAfterSorting(HttpSession session, UserSortingOption sortingOption) {
        session.setAttribute("userSortingOption", sortingOption);
        return "redirect:%s/admins/page{pageNumber}"
                .formatted(ControllerBaseUrlConstants.FOR_USERS_CONTROLLER);
    }*/

    private List<User> sorted(UserSortingOption sortingOption, Integer pageNumber) {
        return sortingOption != null
                ? service.onPage(service.findAllSorted(sortingOption), pageNumber)
                : service.onPage(service.findAllSortedByDefault(), pageNumber);
    }

    private List<User> sorted(UserSortingOption sortingOption, String roleName, Integer pageNumber) {
        return sortingOption != null
                ? service.onPage(service.findAllByRoleNameSorted(roleName, sortingOption), pageNumber)
                : service.onPage(service.findAlByRoleNameSortedByDefault(roleName), pageNumber);
    }

}
