package org.forum.main.services.interfaces;

import org.forum.auxiliary.sorting.options.UserSortingOption;
import org.forum.main.entities.Role;
import org.forum.main.entities.User;
import org.forum.main.services.interfaces.common.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService extends GeneralService<User, Integer>, ValidationService<User>,
        PaginationService<User>, SortingService<User, UserSortingOption>, SearchingService<User> {

    void save(User user, MultipartFile file);

    List<User> findAllByRoleName(String roleName);

    List<User> findAllByRoleNameSorted(String roleName, UserSortingOption sortingOption);

    List<User> findAllByRoleNameSorted(String roleName);

    void changeRole(User user, Role role);

}
