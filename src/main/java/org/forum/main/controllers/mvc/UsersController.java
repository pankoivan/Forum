package org.forum.main.controllers.mvc;

import jakarta.servlet.http.HttpSession;
import org.forum.auxiliary.sorting.enums.UserSortingProperties;
import org.forum.auxiliary.sorting.options.UserSortingOption;
import org.forum.main.controllers.mvc.common.ConvenientController;
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
@RequestMapping("/users")
public class UsersController extends ConvenientController {

    private final SectionService sectionService;

    private final UserService service;

    @Autowired
    public UsersController(SectionService sectionService, UserService service) {
        this.sectionService = sectionService;
        this.service = service;
    }

    @GetMapping
    public String returnUsersPageWithPagination() {
        return "redirect:/users/page1";
    }

    @GetMapping("/page{pageNumber}")
    public String returnUsersPage(Model model,
                                  Authentication authentication,
                                  @SessionAttribute(value = "messageSortingOption", required = false)
                                      UserSortingOption sortingOption,
                                  @PathVariable("pageNumber") String pathPageNumber) {

        Integer pageNumber = toNonNegativeInteger(pathPageNumber);

        addForHeader(model, authentication, sectionService);
        add(model, "users", sorted(sortingOption, pageNumber));
        add(model, "pagesCount", service.pagesCount(service.findAll()));
        add(model, "currentPage", pageNumber);
        add(model, "sortingObject", service.emptySortingOption());
        add(model, "properties", UserSortingProperties.values());
        add(model, "directions", Sort.Direction.values());

        return "users";
    }

    @PostMapping("/page{pageNumber}/sort")
    public String redirectCurrentPageAfterSorting(HttpSession session, UserSortingOption sortingOption) {
        session.setAttribute("userSortingOption", sortingOption);
        return "redirect:/users/page{pageNumber}";
    }

    private List<User> sorted(UserSortingOption sortingOption, Integer pageNumber) {
        return sortingOption != null
                ? service.onPage(service.findAllSorted(sortingOption), pageNumber)
                : service.onPage(service.findAllSortedByDefault(), pageNumber);
    }

}
