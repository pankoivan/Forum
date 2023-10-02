package org.forum.main.services.interfaces;

import org.forum.main.entities.Message;
import org.forum.main.entities.User;

public interface LikeService {

    void save(Message likedMessage, User userWhoLiked);

}
