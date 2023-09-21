package org.forum.main.services.implementations;

import org.forum.auxiliary.constants.DefaultSortingOptionConstants;
import org.forum.auxiliary.exceptions.common.AuxiliaryInstrumentsException;
import org.forum.auxiliary.sorting.options.MessageSortingOption;
import org.forum.main.entities.Message;
import org.forum.main.entities.Topic;
import org.forum.auxiliary.exceptions.ServiceException;
import org.forum.main.services.implementations.common.AbstractPaginationServiceImpl;
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
import java.util.function.Supplier;

@Service
public class MessageServiceImpl extends AbstractPaginationServiceImpl<Message> implements MessageService {

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
        return message.likesCount() - message.dislikesCount() > 0
                ? "Вы не можете удалить ваше сообщение, так как его репутация не отрицательна"
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
                .orElseThrow(() -> new ServiceException("Message with id \"" + id + "\" doesn't exists"));
    }

    @Override
    public List<Message> findAll() {
        return repository.findAll();
    }

    @Override
    public MessageSortingOption emptySortingOption() {
        return new MessageSortingOption();
    }

    @Override
    public List<Message> findAllSorted(MessageSortingOption option) {
        return mySwitch(option,
                () -> repository.findAll(Sort.by(option.getDirection(), "creationDate")),
                () -> repository.findAll(Sort.by(option.getDirection(), "editingDate")),
                () -> repository.findAllByOrderByLikesCountWithDirection(option.getDirection().name()),
                () -> repository.findAllByOrderByDislikesCountWithDirection(option.getDirection().name()));
    }

    @Override
    public List<Message> findAllSortedByDefault() {
        return findAllSorted(DefaultSortingOptionConstants.FOR_MESSAGES);
    }

    @Override
    public List<Message> findAllByTopicId(Integer topicId) {
        return repository.findAllByTopicId(topicId);
    }

    @Override
    public List<Message> findAllByTopicIdSorted(Integer topicId, MessageSortingOption option) {
        return mySwitch(option,
                () -> repository.findAllByTopicId(topicId, Sort.by(option.getDirection(), "creationDate")),
                () -> repository.findAllByTopicId(topicId, Sort.by(option.getDirection(), "editingDate")),
                () -> repository.findAllByTopicIdOrderByLikesCountWithDirection(topicId, option.getDirection().name()),
                () -> repository.findAllByTopicIdOrderByDislikesCountWithDirection(topicId, option.getDirection().name()));
    }

    @Override
    public List<Message> findAllByTopicIdSortedByDefault(Integer topicId) {
        return findAllByTopicIdSorted(topicId, DefaultSortingOptionConstants.FOR_MESSAGES);
    }

    @Override
    public void save(Message message, Authentication authentication, Topic topic) throws ServiceException {
        try {
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
        } catch (AuxiliaryInstrumentsException e) {
            throw new ServiceException("Author cannot be set to message", e);
        }
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<Message> onPage(List<Message> messages, int pageNumber) {
        return onPageImpl(messages, pageNumber, PaginationConstants.MESSAGES);
    }

    @Override
    public int pagesCount(List<Message> messages) {
        return pagesCountImpl(messages, PaginationConstants.MESSAGES);
    }

    @SafeVarargs
    private List<Message> mySwitch(MessageSortingOption option, Supplier<List<Message>> ... suppliers) {
        return switch (option.getProperty()) {
            case BY_CREATION_DATE -> suppliers[0].get();
            case BY_EDITING_DATE -> suppliers[1].get();
            case BY_LIKES_COUNT -> suppliers[2].get();
            case BY_DISLIKES_COUNT -> suppliers[3].get();
        };
    }

}
