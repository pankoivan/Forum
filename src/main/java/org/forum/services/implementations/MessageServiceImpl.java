package org.forum.services.implementations;

import org.forum.entities.Message;
import org.forum.entities.Topic;
import org.forum.repositories.MessageRepository;
import org.forum.services.interfaces.MessageService;
import org.forum.utils.AuthenticationUtils;
import org.forum.utils.constants.PaginationConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRepository repository;

    @Autowired
    public MessageServiceImpl(MessageRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean savingValidation(Message message, BindingResult bindingResult) {
        return bindingResult.hasErrors();
    }

    @Override
    public String deletingValidation(Message message) {
        return message.hasLikes()
                ? "Вы не можете удалить ваше сообщение, так как ему поставлены лайки"
                : null;
    }

    @Override
    public Message empty() {
        return new Message();
    }

    @Override
    public boolean isNew(Message message) {
        return message.getId() == null || !repository.existsById(message.getId());
    }

    @Override
    public Message findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Message with id \"" + id + "\" doesn't exists"));
    }

    @Override
    public List<Message> findAll() {
        return repository.findAll();
    }

    @Override
    public List<Message> findAllByTopicId(Integer topicId) {
        return repository.findAllByTopicId(topicId);
    }

    @Override
    public void save(Message message, Authentication authentication, Topic topic) {
        message.setCreationDate(LocalDateTime.now());
        message.setUserWhoPosted(AuthenticationUtils.extractCurrentUser(authentication));
        message.setTopic(topic);
        repository.save(message);
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<Message> extractForPage(List<Message> messages, int pageNumber) {
        return messages.stream()
                .skip((long) (pageNumber - 1) * PaginationConstants.MESSAGES)
                .limit(PaginationConstants.MESSAGES)
                .toList();
    }

    @Override
    public int calculatePagesCount(List<Message> messages) {
        return Math.ceilDiv(messages.size(), PaginationConstants.MESSAGES);
    }

}