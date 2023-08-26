package org.forum.services.implementations;

import org.forum.entities.User;
import org.forum.entities.enums.Gender;
import org.forum.services.interfaces.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public User newUser() {
        return new User();
    }

    @Override
    public List<Gender> genders() {
        return List.of(Gender.values());
    }

}
