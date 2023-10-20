package org.forum.main.services.interfaces;

import org.forum.main.entities.Dislike;
import org.forum.main.entities.Message;
import org.forum.main.entities.User;

public interface DislikeService {

    Dislike findByMessageAndUser(Message dislikedMessage, User userWhoDisliked);

    void save(Message dislikedMessage, User userWhoDisliked);

    void delete(Message dislikedMessage, User userWhoDisliked);

    void saveOrDelete(Message dislikedMessage, User userWhoDisliked, boolean isCancellation);

}
