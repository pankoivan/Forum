package org.forum.services.implementations;

import org.forum.entities.Section;
import org.forum.repositories.SectionRepository;
import org.forum.services.interfaces.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SectionServiceImpl implements SectionService {

    private final SectionRepository repository;

    @Autowired
    public SectionServiceImpl(SectionRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean savingValidation(Section section, BindingResult bindingResult) {
        if (isNew(section)) {
            if (repository.existsByName(section.getName())) {
                bindingResult.addError(new ObjectError("existsByName",
                        "Раздел с таким названием уже существует"));
                return true;
            }
            if (repository.existsByDescription(section.getDescription())) {
                bindingResult.addError(new ObjectError("existsByDescription",
                        "Раздел с таким описанием уже существует"));
                return true;
            }
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
    public void save(Section section) {
        section.setCreationDate(LocalDateTime.now());
        repository.save(section);
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

}
