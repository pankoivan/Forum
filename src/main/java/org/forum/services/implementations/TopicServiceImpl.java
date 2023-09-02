package org.forum.services.implementations;

import org.forum.entities.Section;
import org.forum.entities.Topic;
import org.forum.repositories.TopicRepository;
import org.forum.services.interfaces.TopicService;
import org.forum.utils.AuthenticationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TopicServiceImpl implements TopicService {

    private final TopicRepository repository;

    @Autowired
    public TopicServiceImpl(TopicRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean savingValidation(Topic topic, BindingResult bindingResult) {
        if (isNew(topic)) {
            if (repository.existsByName(topic.getName())) {
                bindingResult.addError(new ObjectError("existsByName",
                        "Тема с таким названием уже существует"));
                return true;
            }
            if (repository.existsByDescription(topic.getDescription())) {
                bindingResult.addError(new ObjectError("existsByDescription",
                        "Тема с таким описанием уже существует"));
                return true;
            }
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
        return repository.findAll();
    }

    @Override
    public List<Topic> findAllBySectionId(Integer sectionId) {
        return repository.findAllBySectionId(sectionId);
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

}
