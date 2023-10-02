package org.forum.main.services.interfaces;

import org.forum.main.entities.Message;
import org.forum.main.entities.User;

public interface DislikeService {

    void save(Message dislikedMessage, User userWhoDisliked);

}
