package org.forum.main.services.interfaces;

import org.forum.main.entities.Like;
import org.forum.main.entities.Message;
import org.forum.main.entities.User;

public interface LikeService {

    Like findByMessageIdAndUserId(Long messageId, Integer userId);

    void save(Message likedMessage, User userWhoLiked);

    void cancel(Message likedMessage, User userWhoLiked);

    void saveOrCancel(Message likedMessage, User userWhoLiked, boolean isCancellation);

}
