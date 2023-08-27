package org.forum.services.interfaces;

import org.forum.entities.User;
import org.forum.entities.enums.Gender;
import org.forum.services.interfaces.common.CommonService;
import org.forum.services.interfaces.common.ValidationService;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService extends CommonService<User, Integer>, ValidationService<User> {

    List<Gender> genders();

    void save(User user, MultipartFile file);

}
