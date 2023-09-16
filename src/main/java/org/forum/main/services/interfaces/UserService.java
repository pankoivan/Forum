package org.forum.main.services.interfaces;

import org.forum.auxiliary.sorting.options.UserSortingOption;
import org.forum.main.entities.User;
import org.forum.main.entities.enums.Gender;
import org.forum.main.services.interfaces.common.GeneralService;
import org.forum.main.services.interfaces.common.PaginationService;
import org.forum.main.services.interfaces.common.SortingService;
import org.forum.main.services.interfaces.common.ValidationService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService extends GeneralService<User, Integer>, ValidationService<User>,
        SortingService<User, UserSortingOption>, PaginationService<User> {

    List<Gender> genders();

    void save(User user, MultipartFile file);

    List<User> findAllByRoleName(String roleName);

}
