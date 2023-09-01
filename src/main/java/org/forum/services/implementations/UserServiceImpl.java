package org.forum.services.implementations;

import org.forum.entities.User;
import org.forum.entities.enums.Gender;
import org.forum.repositories.UserRepository;
import org.forum.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    @Autowired
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
    public void save(User user, MultipartFile file) {

    }

    @Override
    public void deleteById(Integer id) {

    }

}