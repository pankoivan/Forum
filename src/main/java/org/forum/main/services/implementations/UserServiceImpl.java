package org.forum.main.services.implementations;

import org.forum.auxiliary.constants.DefaultSortingOptionConstants;
import org.forum.auxiliary.sorting.SortingOption;
import org.forum.auxiliary.sorting.options.UserSortingOption;
import org.forum.auxiliary.sorting.enums.UserSortingProperties;
import org.forum.main.entities.User;
import org.forum.main.entities.enums.Gender;
import org.forum.main.repositories.UserRepository;
import org.forum.main.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
        return new User();
    }

    @Override
    public List<Gender> genders() {
        return List.of(Gender.values());
    }

    @Override
    public boolean isNew(User user) {
        return user.getId() == null || !repository.existsById(user.getId());
    }

    @Override
    public User findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User with id \"" + id + "\" doesn't exists"));
    }

    @Override
    public List<User> findAll() {
        return repository.findAll();
    }

    @Override
    public SortingOption<UserSortingProperties> emptySortingOption() {
        return new UserSortingOption();
    }

    @Override
    public List<User> findAllSorted(SortingOption<UserSortingProperties> option) {
        return switch(option.getProperty()) {

            case BY_NICKNAME -> repository.findAll(Sort.by(option.getDirection(), "nickname"));

            case BY_REGISTRATION_DATE -> repository.findAll(Sort.by(option.getDirection(), "registration_date"));

            case BY_MESSAGES_COUNT -> repository.findAllByOrderByMessagesCountWithDirection(option.getDirection().name());

            case BY_LIKES_COUNT -> repository.findAllByOrderByLikesCountWithDirection(option.getDirection().name());

        };
    }

    @Override
    public List<User> findAllSortedByDefault() {
        return findAllSorted(DefaultSortingOptionConstants.FOR_USERS);
    }

    @Override
    public List<User> findAllByRoleName(String roleName) {
        return repository.findAllByRoleName(roleName);
    }

    @Override
    public void save(User user, MultipartFile file) {

    }

    @Override
    public void deleteById(Integer id) {

    }

}
