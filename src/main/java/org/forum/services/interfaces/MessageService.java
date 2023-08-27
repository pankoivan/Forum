package org.forum.services.interfaces;

import org.forum.entities.Message;
import org.forum.entities.Topic;
import org.forum.services.interfaces.common.CommonService;
import org.forum.services.interfaces.common.ValidationService;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface MessageService extends CommonService<Message, Long>, ValidationService<Message> {

    void save(Message message, Authentication authentication, Topic topic);

    List<Message> findAllByTopicId(Integer topicId);

}
