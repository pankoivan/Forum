package org.forum.main.services.implementations;

import org.forum.main.entities.Section;
import org.forum.main.entities.Topic;
import org.forum.main.services.interfaces.TopicService;
import org.forum.main.repositories.TopicRepository;
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
public class TopicServiceImpl implements TopicService {

    private final TopicRepository repository;

    @Autowired
    public TopicServiceImpl(TopicRepository repository) {
        this.repository = repository;
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
                ? "Тему \"" + topic.getName() + "\" нельзя удалять, так как в ней содержатся какие-то сообщения"
                : null;
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
                .orElseThrow(() -> new RuntimeException("Topic with id \"" + id + "\" doesn't exists"));
    }

    @Override
    public List<Topic> findAll() {
        return repository.findAll(Sort.by(Sort.Direction.ASC, "name"));
    }

    @Override
    public List<Topic> findAllBySectionId(Integer sectionId) {
        return repository.findAllBySectionId(sectionId, Sort.by(Sort.Direction.ASC, "name"));
    }

    @Override
    public void save(Topic topic, Authentication authentication, Section section) {
        if (isNew(topic)) {
            topic.setCreationDate(LocalDateTime.now());
            topic.setUserWhoCreated(AuthenticationUtils.extractCurrentUser(authentication));
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
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    private boolean savingValidationByName(Topic topic) {
        Optional<Topic> foundSection = repository.findByName(topic.getName());
        return foundSection.isPresent() && !foundSection.get().getId().equals(topic.getId());
    }

    private boolean savingValidationByDescription(Topic topic) {
        Optional<Topic> foundSection = repository.findByDescription(topic.getName());
        return foundSection.isPresent() && !foundSection.get().getId().equals(topic.getId());
    }

}