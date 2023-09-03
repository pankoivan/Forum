package org.forum.services.implementations;

import org.forum.entities.Authority;
import org.forum.repositories.AuthorityRepository;
import org.forum.services.interfaces.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.List;

@Service
public class AuthorityServiceImpl implements AuthorityService {

    private final AuthorityRepository repository;

    @Autowired
    public AuthorityServiceImpl(AuthorityRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean savingValidation(Authority authority, BindingResult bindingResult) {
        if (isNew(authority) && repository.existsByName(authority.getName())) {
            bindingResult.addError(new ObjectError("existsByName",
                    "Право с таким названием уже существует"));
            return true;
        }
        return bindingResult.hasErrors();
    }

    @Override
    public String deletingValidation(Authority authority) {
        return authority.getRoles().stream()
                .anyMatch(role -> role.authoritiesCount() == 1)
                ? "Право \"" + authority.getName() + "\" нельзя удалять, так как для каких-то ролей оно является единственным"
                : null;
    }

    @Override
    public Authority empty() {
        return new Authority();
    }

    @Override
    public boolean isNew(Authority authority) {
        return authority.getId() == null || !repository.existsById(authority.getId());
    }

    @Override
    public Authority findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Authority with id \"" + id + "\" doesn't exists"));
    }

    @Override
    public List<Authority> findAll() {
        return repository.findAllByOrderByNameAsc();
    }

    @Override
    public void save(Authority authority) {
        repository.save(authority);
    }

    @Override
    public void deleteById(Integer id) {
        Authority authority = findById(id);
        authority.getRoles().forEach(role -> role.removeAuthority(authority));
        repository.deleteById(id);
    }

}
