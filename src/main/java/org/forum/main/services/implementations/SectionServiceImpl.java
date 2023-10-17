package org.forum.main.services.implementations;

import org.forum.auxiliary.constants.DefaultSortingOptionConstants;
import org.forum.auxiliary.constants.PaginationConstants;
import org.forum.auxiliary.exceptions.common.AuxiliaryInstrumentsException;
import org.forum.auxiliary.sorting.options.SectionSortingOption;
import org.forum.main.entities.Section;
import org.forum.auxiliary.exceptions.ServiceException;
import org.forum.main.repositories.SectionRepository;
import org.forum.main.services.implementations.common.AbstractPaginationServiceImpl;
import org.forum.main.services.interfaces.SectionService;
import org.forum.auxiliary.utils.AuthenticationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class SectionServiceImpl extends AbstractPaginationServiceImpl<Section> implements SectionService {

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
                .orElseThrow(() -> new ServiceException("Section with id \"" + id + "\" doesn't exists"));
    }

    @Override
    public List<Section> findAll() {
        return repository.findAll();
    }

    @Override
    public SectionSortingOption emptySortingOption() {
        return new SectionSortingOption();
    }

    @Override
    public List<Section> findAllSorted(SectionSortingOption option) {
        return switch (option.getProperty()) {
            case BY_NAME -> repository.findAll(Sort.by(option.getDirection(), "name"));
            case BY_CREATION_DATE -> repository.findAll(Sort.by(option.getDirection(), "creationDate"));
            case BY_TOPICS_COUNT -> repository.findAllByOrderByTopicsCountWithDirection(option.getDirection().name());
            case BY_MESSAGES_COUNT -> repository.findAllByOrderByMessagesCountWithDirection(option.getDirection().name());
        };
    }

    @Override
    public List<Section> findAllSortedByDefault() {
        return findAllSorted(DefaultSortingOptionConstants.FOR_SECTIONS);
    }

    @Override
    public void save(Section section, Authentication authentication) throws ServiceException {
        try {
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
        } catch (AuxiliaryInstrumentsException e) {
            throw new ServiceException("Author cannot be set to section", e);
        }
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public List<Section> onPage(List<Section> foundItems, int pageNumber) {
        return onPageImpl(foundItems, pageNumber, PaginationConstants.FOR_SECTIONS);
    }

    @Override
    public int pagesCount(List<Section> foundItems) {
        return pagesCountImpl(foundItems, PaginationConstants.FOR_SECTIONS);
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
