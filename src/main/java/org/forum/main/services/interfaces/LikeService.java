package org.forum.main.services.interfaces;

import org.forum.main.entities.Like;
import org.forum.main.entities.Message;
import org.forum.main.entities.User;

public interface LikeService {

    Like findByMessageAndUser(Message likedMessage, User userWhoLiked);

    void save(Message likedMessage, User userWhoLiked);

    void delete(Message likedMessage, User userWhoLiked);

    void saveOrDelete(Message likedMessage, User userWhoLiked, boolean isCancellation);

}
