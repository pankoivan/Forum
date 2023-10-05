package org.forum.main.services.interfaces;

import org.forum.main.entities.Dislike;
import org.forum.main.entities.Message;
import org.forum.main.entities.User;

public interface DislikeService {

    Dislike findByMessageIdAndUserId(Long messageId, Integer userId);

    void save(Message dislikedMessage, User userWhoDisliked);

    void cancel(Message dislikedMessage, User userWhoDisliked);

    void saveOrCancel(Message dislikedMessage, User userWhoDisliked, boolean isCancellation);

}
