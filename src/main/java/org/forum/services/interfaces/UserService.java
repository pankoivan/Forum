package org.forum.services.interfaces;

import org.forum.entities.User;
import org.forum.entities.enums.Gender;

import java.util.List;

public interface UserService {

    User newUser();

    List<Gender> genders();

}
