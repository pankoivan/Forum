package org.forum.main.services.implementations;

import org.forum.auxiliary.constants.sorting.DefaultSortingOptionConstants;
import org.forum.auxiliary.constants.pagination.PaginationConstants;
import org.forum.auxiliary.sorting.options.TopicSortingOption;
import org.forum.auxiliary.utils.SearchingUtils;
import org.forum.main.entities.Section;
import org.forum.main.entities.Topic;
import org.forum.auxiliary.exceptions.ServiceException;
import org.forum.main.entities.User;
import org.forum.main.services.implementations.common.DefaultPaginationImpl;
import org.forum.main.services.interfaces.TopicService;
import org.forum.main.repositories.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Service
public class TopicServiceImpl extends DefaultPaginationImpl<Topic> implements TopicService {

    private final TopicRepository repository;

    @Autowired
    public TopicServiceImpl(TopicRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(Topic topic, User author, Section section) throws ServiceException {
        if (isNew(topic)) {
            topic.setCreationDate(LocalDateTime.now());
            topic.setUserWhoCreated(author);
            topic.setSection(section);
            repository.save(topic);
        } else {
            Topic oldTopic = findById(topic.getId());
            oldTopic.setName(topic.getName());
            oldTopic.setDescription(topic.getDescription());
            repository.save(oldTopic);
        }
    }

    @Override
    public Topic empty() {
        return new Topic();
    }

    @Override
    public boolean isNew(Topic topic) {
        return topic.getId() == null || !repository.existsById(topic.getId());
    }

    @Override
    public Topic findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ServiceException("Topic with id \"%s\" doesn't exists".formatted(id)));
    }

    @Override
    public List<Topic> findAll() {
        return repository.findAll();
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public boolean savingValidation(Topic topic, BindingResult bindingResult) {
        if (savingValidationByName(topic)) {
            bindingResult.addError(new ObjectError("existsByName",
                    "Тема с таким названием уже существует"));
            return true;
        }
        if (savingValidationByDescription(topic)) {
            bindingResult.addError(new ObjectError("existsByDescription",
                    "Тема с таким описанием уже существует"));
            return true;
        }
        return bindingResult.hasErrors();
    }

    @Override
    public String deletingValidation(Topic topic) {
        return topic.hasMessages()
                ? "Тему \"%s\" нельзя удалять, так как в ней содержатся какие-то сообщения".formatted(topic.getName())
                : null;
    }

    @Override
    public List<Topic> onPage(List<Topic> topics, int pageNumber) {
        return onPageImpl(topics, pageNumber, PaginationConstants.FOR_TOPICS);
    }

    @Override
    public int pagesCount(List<Topic> topics) {
        return pagesCountImpl(topics, PaginationConstants.FOR_TOPICS);
    }

    @Override
    public TopicSortingOption emptySortingOption() {
        return new TopicSortingOption();
    }

    @Override
    public List<Topic> findAllSorted(TopicSortingOption option) {
        return mySwitch(option,
                () -> repository.findAll(Sort.by(option.getDirection(), "name")),
                () -> repository.findAll(Sort.by(option.getDirection(), "creationDate")),
                () -> repository.findAllByOrderByMessagesCountWithDirection(option.getDirection().name()));
    }

    @Override
    public List<Topic> findAllSorted() {
        return findAllSorted(DefaultSortingOptionConstants.FOR_TOPICS);
    }

    @Override
    public List<Topic> findAllBySectionId(Integer sectionId) {
        return repository.findAllBySectionId(sectionId);
    }

    @Override
    public List<Topic> findAllBySectionIdSorted(Integer sectionId, TopicSortingOption option) {
        return mySwitch(option,
                () -> repository.findAllBySectionId(sectionId, Sort.by(option.getDirection(), "name")),
                () -> repository.findAllBySectionId(sectionId, Sort.by(option.getDirection(), "creationDate")),
                () -> repository.findAllBySectionIdOrderByMessagesCountWithDirection(sectionId, option.getDirection().name())
        );
    }

    @Override
    public List<Topic> findAllBySectionIdSorted(Integer sectionId) {
        return findAllBySectionIdSorted(sectionId, DefaultSortingOptionConstants.FOR_TOPICS);
    }

    @Override
    public List<Topic> findAllByUserIdSorted(Integer userId, TopicSortingOption option) {
        return mySwitch(option,
                () -> filterByUserId(repository.findAll(Sort.by(option.getDirection(), "name")), userId),
                () -> filterByUserId(repository.findAll(Sort.by(option.getDirection(), "creationDate")), userId),
                () -> filterByUserId(repository.findAllByOrderByMessagesCountWithDirection(option.getDirection().name()), userId)
        );
    }

    @Override
    public List<Topic> findAllByUserIdSorted(Integer userId) {
        return findAllByUserIdSorted(userId, DefaultSortingOptionConstants.FOR_TOPICS);
    }

    @Override
    public List<Topic> search(List<Topic> topics, String searchedString) {
        return topics.stream()
                .filter(topic -> SearchingUtils.search(
                        searchedString,
                        topic.getName(),
                        topic.getDescription(),
                        topic.getUserWhoCreated().getNickname(),
                        topic.getFormattedCreationDate()
                ))
                .toList();
    }

    @SafeVarargs
    private List<Topic> mySwitch(TopicSortingOption option, Supplier<List<Topic>>... suppliers) {
        return switch (option.getProperty()) {
            case BY_NAME -> suppliers[0].get();
            case BY_CREATION_DATE -> suppliers[1].get();
            case BY_MESSAGES_COUNT -> suppliers[2].get();
        };
    }

    private boolean savingValidationByName(Topic topic) {
        Optional<Topic> foundSection = repository.findByName(topic.getName());
        return foundSection.isPresent() && !foundSection.get().getId().equals(topic.getId());
    }

    private boolean savingValidationByDescription(Topic topic) {
        Optional<Topic> foundSection = repository.findByDescription(topic.getName());
        return foundSection.isPresent() && !foundSection.get().getId().equals(topic.getId());
    }

    private List<Topic> filterByUserId(List<Topic> topics, Integer userId) {
        return topics.stream()
                .filter(topic -> topic.getUserWhoCreated().getId().equals(userId))
                .toList();
    }

}
