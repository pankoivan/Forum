package org.forum.main.services.implementations;

import org.forum.auxiliary.constants.DefaultSortingOptionConstants;
import org.forum.auxiliary.constants.pagination.PaginationConstants;
import org.forum.auxiliary.sorting.options.UserSortingOption;
import org.forum.main.entities.Role;
import org.forum.main.entities.User;
import org.forum.auxiliary.exceptions.ServiceException;
import org.forum.main.repositories.UserRepository;
import org.forum.main.services.implementations.common.DefaultPaginationImpl;
import org.forum.main.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.function.Supplier;

@Service
public class UserImpl extends DefaultPaginationImpl<User> implements UserService {

    private final UserRepository repository;

    @Autowired
    public UserImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(User user, MultipartFile file) {

    }

    @Override
    public User empty() {
        return new User();
    }

    @Override
    public boolean isNew(User user) {
        return user.getId() == null || !repository.existsById(user.getId());
    }

    @Override
    public User findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ServiceException("User with id \"%s\" doesn't exists".formatted(id)));
    }

    @Override
    public List<User> findAll() {
        return repository.findAll();
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public UserSortingOption emptySortingOption() {
        return new UserSortingOption();
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
    public List<User> onPage(List<User> users, int pageNumber) {
        return onPageImpl(users, pageNumber, PaginationConstants.FOR_USERS);
    }

    @Override
    public int pagesCount(List<User> users) {
        return pagesCountImpl(users, PaginationConstants.FOR_USERS);
    }

    @Override
    public List<User> findAllSorted(UserSortingOption option) {
        return mySwitch(option,
                () -> repository.findAll(Sort.by(option.getDirection(), "nickname")),
                () -> repository.findAll(Sort.by(option.getDirection(), "registrationDate")),
                () -> repository.findAllByOrderByMessagesCountWithDirection(option.getDirection().name()),
                () -> repository.findAllByOrderByReputationWithDirection(option.getDirection().name()));
    }

    @Override
    public List<User> findAllSorted() {
        return findAllSorted(DefaultSortingOptionConstants.FOR_USERS);
    }

    @Override
    public List<User> findAllByRoleName(String roleName) {
        return repository.findAllByRoleName(roleName);
    }

    @Override
    public List<User> findAllByRoleNameSorted(String roleName, UserSortingOption option) {
        return mySwitch(option,
                () -> repository.findAllByRoleName(roleName, Sort.by(option.getDirection(), "nickname")),
                () -> repository.findAllByRoleName(roleName, Sort.by(option.getDirection(), "registrationDate")),
                () -> repository.findAllByRoleNameOrderByMessagesCountWithDirection(roleName, option.getDirection().name()),
                () -> repository.findAllByRoleNameOrderByReputationWithDirection(roleName, option.getDirection().name()));
    }

    @Override
    public List<User> findAllByRoleNameSorted(String roleName) {
        return findAllByRoleNameSorted(roleName, DefaultSortingOptionConstants.FOR_USERS);
    }

    @Override
    public void changeRole(User user, Role role) {
        user.setRole(role);
        repository.save(user);
    }

    @SafeVarargs
    private List<User> mySwitch(UserSortingOption option, Supplier<List<User>>... suppliers) {
        return switch (option.getProperty()) {
            case BY_NICKNAME -> suppliers[0].get();
            case BY_REGISTRATION_DATE -> suppliers[1].get();
            case BY_MESSAGES_COUNT -> suppliers[2].get();
            case BY_REPUTATION -> suppliers[3].get();
        };
    }

}
