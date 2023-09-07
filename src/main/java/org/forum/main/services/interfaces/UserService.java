package org.forum.main.services.interfaces;

import org.forum.auxiliary.sorting.SortingOption;
import org.forum.auxiliary.sorting.enums.UserSortingProperties;
import org.forum.main.entities.User;
import org.forum.main.entities.enums.Gender;
import org.forum.main.services.interfaces.common.GeneralService;
import org.forum.main.services.interfaces.common.SortingService;
import org.forum.main.services.interfaces.common.ValidationService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService extends GeneralService<User, Integer>, ValidationService<User>,
        SortingService<User, SortingOption<UserSortingProperties>> {

    List<Gender> genders();

    void save(User user, MultipartFile file);

    List<User> findAllByRoleName(String roleName);

}
