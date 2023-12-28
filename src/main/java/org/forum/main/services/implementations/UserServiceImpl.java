package org.forum.main.services.implementations;

import org.forum.auxiliary.constants.sorting.DefaultSortingOptionConstants;
import org.forum.auxiliary.constants.pagination.PaginationConstants;
import org.forum.auxiliary.sorting.options.UserSortingOption;
import org.forum.auxiliary.utils.SearchingUtils;
import org.forum.main.entities.Role;
import org.forum.main.entities.User;
import org.forum.auxiliary.exceptions.ServiceException;
import org.forum.main.repositories.UserInformationRepository;
import org.forum.main.repositories.UserRepository;
import org.forum.main.services.implementations.common.DefaultPaginationImpl;
import org.forum.main.services.interfaces.RoleService;
import org.forum.main.services.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Service
public class UserServiceImpl extends DefaultPaginationImpl<User> implements UserService {

    private final UserRepository repository;

    private final UserInformationRepository userInformationRepository;

    private final RoleService roleService;

    private final PasswordEncoder passwordEncoder;

    private final String uploadPath;

    @Autowired
    public UserServiceImpl(UserRepository repository, UserInformationRepository userInformationRepository,
                           RoleService roleService, PasswordEncoder passwordEncoder, @Value("${my.upload}") String uploadPath) {

        this.repository = repository;
        this.userInformationRepository = userInformationRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.uploadPath = uploadPath;
    }

    @Override
    public void save(User user, MultipartFile file) {
        String linkToImage = saveAvatar(user, file);
        user.getUserInformation().setLinkToImage(linkToImage);
        user.setRegistrationDate(LocalDateTime.now());
        user.setRole(roleService.findByName("ROLE_USER"));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        repository.save(user);
        user.getUserInformation().setUser(user);
        userInformationRepository.save(user.getUserInformation());
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
    public boolean savingValidation(User user, BindingResult bindingResult) {
        if (savingValidationByEmail(user)) {
            bindingResult.addError(new ObjectError("existsByEmail",
                    "Пользователь с такой почтой уже существует"));
            return true;
        }
        if (savingValidationByUsername(user)) {
            bindingResult.addError(new ObjectError("existsByUsername",
                    "Пользователь с таким именем уже существует"));
            return true;
        }
        return bindingResult.hasErrors();
    }

    @Override
    public String deletingValidation(User user) {
        return user.getReputation() >= 0
                ? "Пользователя \"%s\" нельзя удалять, так как его репутация неотрицательна".formatted(user.getNickname())
                : null;
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
    public UserSortingOption emptySortingOption() {
        return new UserSortingOption();
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
                () -> filterByRoleName(repository.findAllByOrderByMessagesCountWithDirection(option.getDirection().name()), roleName),
                () -> filterByRoleName(repository.findAllByOrderByReputationWithDirection(option.getDirection().name()), roleName)
        );
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

    @Override
    public List<User> search(List<User> users, String searchedString) {
        return users.stream()
                .filter(user -> SearchingUtils.search(
                        searchedString,
                        user.getNickname(),
                        user.getFormattedRegistrationDate(),
                        user.getRole().getAlias()
                ))
                .toList();
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

    private List<User> filterByRoleName(List<User> users, String roleName) {
        return users.stream()
                .filter(user -> user.hasRole(roleName))
                .toList();
    }

    private boolean savingValidationByUsername(User user) {
        Optional<User> foundUser = repository.findByNickname(user.getNickname());
        return foundUser.isPresent() && !foundUser.get().getId().equals(user.getId());
    }

    private boolean savingValidationByEmail(User user) {
        Optional<User> foundUser = repository.findByEmail(user.getEmail());
        return foundUser.isPresent() && !foundUser.get().getId().equals(user.getId());
    }

    private String saveAvatar(User user, MultipartFile avatar) {
        String filename = "%s %s %s".formatted(
                user.getEmail(),
                LocalDateTime.now().toString().replaceAll(":", "."),
                avatar.getOriginalFilename()
        );
        Path path = Paths.get(uploadPath, filename);
        try {
            avatar.transferTo(path);
        } catch (IOException e) {
            throw new ServiceException(e.getMessage(), e);
        }
        return path.toString();
    }

}
