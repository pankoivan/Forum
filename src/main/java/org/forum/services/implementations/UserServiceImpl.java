package org.forum.services.implementations;

import org.forum.entities.User;
import org.forum.entities.enums.Gender;
import org.forum.repositories.UserRepository;
import org.forum.services.interfaces.UserService;
import org.forum.services.interfaces.common.ValidationService;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;

@Service
public class UserServiceImpl implements UserService, ValidationService<User> {

    private final UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean savingValidation(User user, BindingResult bindingResult) {
        return false;
    }

    @Override
    public String deletingValidation(User user) {
        return null;
    }

    @Override
    public User empty() {
        return null;
    }

    @Override
    public List<Gender> genders() {
        return List.of(Gender.values());
    }

    @Override
    public boolean isNew(User user) {
        return false;
    }

    @Override
    public User findById(Integer id) {
        return null;
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public void save(User user) {

    }

    @Override
    public void deleteById(Integer id) {

    }

}
