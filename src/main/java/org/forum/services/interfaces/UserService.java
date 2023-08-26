package org.forum.services.interfaces;

import org.forum.entities.User;
import org.forum.entities.enums.Gender;

import java.util.List;

public interface UserService extends CommonService<User, Integer> {

    List<Gender> genders();

}
