package org.forum.main.services.implementations;

import org.forum.auxiliary.constants.DefaultSortingOptionConstants;
import org.forum.auxiliary.sorting.options.MessageSortingOption;
import org.forum.auxiliary.sorting.SortingOption;
import org.forum.auxiliary.sorting.enums.MessageSortingProperties;
import org.forum.main.entities.Message;
import org.forum.main.entities.Topic;
import org.forum.main.services.interfaces.MessageService;
import org.forum.main.repositories.MessageRepository;
import org.forum.auxiliary.utils.AuthenticationUtils;
import org.forum.auxiliary.constants.PaginationConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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
        return repository.findAll(Sort.by(Sort.Direction.ASC, "creationDate"));
    }

    @Override
    public SortingOption<MessageSortingProperties> emptySortingOption() {
        return new MessageSortingOption();
    }

    @Override
    public List<Message> findAllSorted(SortingOption<MessageSortingProperties> option) {
        return switch (option.getProperty()) {

            case BY_CREATION_DATE -> repository.findAll(Sort.by(option.getDirection(), "creationDate"));

            case BY_EDITING_DATE -> repository.findAll(Sort.by(option.getDirection(), "editingDate"));

            case BY_LIKES_COUNT -> repository.findAllByOrderByLikesCountWithDirection(option.getDirection().name());

        };
    }

    @Override
    public List<Message> findAllSortedByDefault() {
        return findAllSorted(DefaultSortingOptionConstants.FOR_MESSAGES);
    }

    @Override
    public List<Message> findAllByTopicId(Integer topicId) {
        return repository.findAllByTopicId(topicId, Sort.by(Sort.Direction.ASC, "creationDate"));
    }

    @Override
    public void save(Message message, Authentication authentication, Topic topic) {
        if (isNew(message)) {
            message.setCreationDate(LocalDateTime.now());
            message.setUserWhoPosted(AuthenticationUtils.extractCurrentUser(authentication));
            message.setTopic(topic);
            repository.save(message);
        } else {
            Message oldMessage = findById(message.getId());
            oldMessage.setEditingDate(LocalDateTime.now());
            oldMessage.setText(message.getText());
            repository.save(oldMessage);
        }
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<Message> onPage(List<Message> messages, int pageNumber) {
        return messages.stream()
                .skip((long) (pageNumber - 1) * PaginationConstants.MESSAGES)
                .limit(PaginationConstants.MESSAGES)
                .toList();
    }

    @Override
    public int pagesCount(List<Message> messages) {
        return !messages.isEmpty()
                ? Math.ceilDiv(messages.size(), PaginationConstants.MESSAGES)
                : 1;
    }

}
