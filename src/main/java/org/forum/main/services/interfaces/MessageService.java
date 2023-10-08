package org.forum.main.services.interfaces;

import org.forum.auxiliary.sorting.options.MessageSortingOption;
import org.forum.main.entities.Message;
import org.forum.main.entities.Topic;
import org.forum.main.services.interfaces.common.SortingService;
import org.forum.main.services.interfaces.common.ValidationService;
import org.forum.main.services.interfaces.common.GeneralService;
import org.forum.main.services.interfaces.common.PaginationService;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface MessageService extends GeneralService<Message, Long>, ValidationService<Message>,
        PaginationService<Message>, SortingService<Message, MessageSortingOption> {

    void save(Message message, Authentication authentication, Topic topic);

    List<Message> findAllByTopicId(Integer topicId);

    List<Message> findAllByTopicIdSorted(Integer topicId, MessageSortingOption sortingOption);

    List<Message> findAllByTopicIdSortedByDefault(Integer topicId);

    List<Message> findAllByUserIdSorted(Integer userId, MessageSortingOption sortingOption);

    List<Message> findAllByUserIdSortedByDefault(Integer userId);

    List<Message> findAllLikedByUserIdSorted(Integer userId, MessageSortingOption sortingOption);

    List<Message> findAllLikedByUserIdSortedByDefault(Integer userId);

    List<Message> findAllDislikedByUserIdSorted(Integer userId, MessageSortingOption sortingOption);

    List<Message> findAllDislikedByUserIdSortedByDefault(Integer userId);
}
