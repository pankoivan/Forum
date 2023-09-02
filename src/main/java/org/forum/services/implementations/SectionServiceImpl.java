package org.forum.services.implementations;

import org.forum.entities.Section;
import org.forum.repositories.SectionRepository;
import org.forum.services.interfaces.SectionService;
import org.forum.utils.AuthenticationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SectionServiceImpl implements SectionService {

    private final SectionRepository repository;

    @Autowired
    public SectionServiceImpl(SectionRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean savingValidation(Section section, BindingResult bindingResult) {
        if (savingValidationByName(section)) {
            bindingResult.addError(new ObjectError("existsByName",
                    "Раздел с таким названием уже существует"));
            return true;
        }
        if (savingValidationByDescription(section)) {
            bindingResult.addError(new ObjectError("existsByDescription",
                    "Раздел с таким описанием уже существует"));
            return true;
        }
        return bindingResult.hasErrors();
    }

    @Override
    public String deletingValidation(Section section) {
        return section.hasTopics()
                ? "Раздел \"" + section.getName() + "\" нельзя удалять, так как в нём содержатся какие-то темы"
                : null;
    }

    @Override
    public Section empty() {
        return new Section();
    }

    @Override
    public boolean isNew(Section section) {
        return section.getId() == null || !repository.existsById(section.getId());
    }

    @Override
    public Section findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Section with id \"" + id + "\" doesn't exists"));
    }

    @Override
    public List<Section> findAll() {
        return repository.findAll();
    }

    @Override
    public void save(Section section, Authentication authentication) {
        if (isNew(section)) {
            section.setCreationDate(LocalDateTime.now());
            section.setUserWhoCreated(AuthenticationUtils.extractCurrentUser(authentication));
            repository.save(section);
        } else {
            Section oldSection = findById(section.getId());
            oldSection.setName(section.getName());
            oldSection.setDescription(section.getDescription());
            repository.save(oldSection);
        }
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    private boolean savingValidationByName(Section section) {
        Optional<Section> foundSection = repository.findByName(section.getName());
        return foundSection.isPresent() && !foundSection.get().getId().equals(section.getId());
    }

    private boolean savingValidationByDescription(Section section) {
        Optional<Section> foundSection = repository.findByDescription(section.getName());
        return foundSection.isPresent() && !foundSection.get().getId().equals(section.getId());
    }

}
