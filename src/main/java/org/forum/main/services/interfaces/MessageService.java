package org.forum.main.services.interfaces;

import org.forum.main.entities.Message;
import org.forum.main.entities.Topic;
import org.forum.main.services.interfaces.common.ValidationService;
import org.forum.main.services.interfaces.common.GeneralService;
import org.forum.main.services.interfaces.common.PaginationService;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface MessageService extends GeneralService<Message, Long>, ValidationService<Message>,
        PaginationService<Message> {

    void save(Message message, Authentication authentication, Topic topic);

    List<Message> findAllByTopicId(Integer topicId);

}
