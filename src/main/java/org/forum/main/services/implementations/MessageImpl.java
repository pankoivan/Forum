package org.forum.main.services.implementations;

import org.forum.auxiliary.constants.sorting.DefaultSortingOptionConstants;
import org.forum.auxiliary.exceptions.common.AuxiliaryInstrumentsException;
import org.forum.auxiliary.sorting.options.MessageSortingOption;
import org.forum.main.entities.Message;
import org.forum.main.entities.Topic;
import org.forum.auxiliary.exceptions.ServiceException;
import org.forum.main.services.implementations.common.DefaultPaginationImpl;
import org.forum.main.services.interfaces.MessageService;
import org.forum.main.repositories.MessageRepository;
import org.forum.auxiliary.utils.AuthenticationUtils;
import org.forum.auxiliary.constants.pagination.PaginationConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Supplier;

@Service
public class MessageImpl extends DefaultPaginationImpl<Message> implements MessageService {

    private final MessageRepository repository;

    @Autowired
    public MessageImpl(MessageRepository repository) {
        this.repository = repository;
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
                .orElseThrow(() -> new ServiceException("Message with id \"%s\" doesn't exists".formatted(id)));
    }

    @Override
    public List<Message> findAll() {
        return repository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public boolean savingValidation(Message message, BindingResult bindingResult) {
        return bindingResult.hasErrors();
    }

    @Override
    public String deletingValidation(Message message) {
        return message.likesCount() - message.dislikesCount() > 0
                ? "Вы не можете удалить сообщение, так как его репутация не отрицательна"
                : null;
    }

    @Override
    public List<Message> onPage(List<Message> messages, int pageNumber) {
        return onPageImpl(messages, pageNumber, PaginationConstants.FOR_MESSAGES);
    }

    @Override
    public int pagesCount(List<Message> messages) {
        return pagesCountImpl(messages, PaginationConstants.FOR_MESSAGES);
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
                () -> repository.findAllByOrderByDislikesCountWithDirection(option.getDirection().name())
        );
    }

    @Override
    public List<Message> findAllSorted() {
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
                () -> repository.findAllByTopicIdOrderByDislikesCountWithDirection(topicId, option.getDirection().name())
        );
    }

    @Override
    public List<Message> findAllByTopicIdSorted(Integer topicId) {
        return findAllByTopicIdSorted(topicId, DefaultSortingOptionConstants.FOR_MESSAGES);
    }

    @Override
    public List<Message> findAllByUserId(Integer userId) {
        return filterByUserId(repository.findAll(), userId);
    }

    @Override
    public List<Message> findAllByUserIdSorted(Integer userId, MessageSortingOption option) {
        return mySwitch(option,
                () -> filterByUserId(repository.findAll(Sort.by(option.getDirection(), "creationDate")),
                        userId),
                () -> filterByUserId(repository.findAll(Sort.by(option.getDirection(), "editingDate")),
                        userId),
                () -> filterByUserId(repository.findAllByOrderByLikesCountWithDirection(option.getDirection().name()),
                        userId),
                () -> filterByUserId(repository.findAllByOrderByDislikesCountWithDirection(option.getDirection().name()),
                        userId)
        );
    }

    @Override
    public List<Message> findAllByUserIdSorted(Integer userId) {
        return findAllByUserIdSorted(userId, DefaultSortingOptionConstants.FOR_MESSAGES);
    }

    @Override
    public List<Message> findAllLikedByUserId(Integer userId) {
        return filterLikedByUserId(repository.findAll(), userId);
    }

    @Override
    public List<Message> findAllLikedByUserIdSorted(Integer userId, MessageSortingOption option) {
        return mySwitch(option,
                () -> filterLikedByUserId(repository.findAll(Sort.by(option.getDirection(), "creationDate")),
                        userId),
                () -> filterLikedByUserId(repository.findAll(Sort.by(option.getDirection(), "editingDate")),
                        userId),
                () -> filterLikedByUserId(repository.findAllByOrderByLikesCountWithDirection(option.getDirection().name()),
                        userId),
                () -> filterLikedByUserId(repository.findAllByOrderByDislikesCountWithDirection(option.getDirection().name()),
                        userId)
        );
    }

    @Override
    public List<Message> findAllLikedByUserIdSorted(Integer userId) {
        return findAllLikedByUserIdSorted(userId, DefaultSortingOptionConstants.FOR_MESSAGES);
    }

    @Override
    public List<Message> findAllDislikedByUserId(Integer userId) {
        return filterLikedByUserId(repository.findAll(), userId);
    }

    @Override
    public List<Message> findAllDislikedByUserIdSorted(Integer userId, MessageSortingOption option) {
        return mySwitch(option,
                () -> filterDislikedByUserId(repository.findAll(Sort.by(option.getDirection(), "creationDate")),
                        userId),
                () -> filterDislikedByUserId(repository.findAll(Sort.by(option.getDirection(), "editingDate")),
                        userId),
                () -> filterDislikedByUserId(repository.findAllByOrderByLikesCountWithDirection(option.getDirection().name()),
                        userId),
                () -> filterDislikedByUserId(repository.findAllByOrderByDislikesCountWithDirection(option.getDirection().name()),
                        userId)
        );
    }

    @Override
    public List<Message> findAllDislikedByUserIdSorted(Integer userId) {
        return findAllDislikedByUserIdSorted(userId, DefaultSortingOptionConstants.FOR_MESSAGES);
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

    private List<Message> filterByUserId(List<Message> messages, Integer userId) {
        return messages.stream()
                .filter(message -> message.getUserWhoPosted().getId().equals(userId))
                .toList();
    }

    private List<Message> filterLikedByUserId(List<Message> messages, Integer userId) {
        return messages.stream()
                .filter(message -> message.containsLikedUserById(userId))
                .toList();
    }

    private List<Message> filterDislikedByUserId(List<Message> messages, Integer userId) {
        return messages.stream()
                .filter(message -> message.containsDislikedUserById(userId))
                .toList();
    }

}
