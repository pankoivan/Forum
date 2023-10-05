package org.forum.main.services.implementations;

import org.forum.auxiliary.exceptions.ServiceException;
import org.forum.main.entities.Like;
import org.forum.main.entities.Message;
import org.forum.main.entities.User;
import org.forum.main.repositories.LikeRepository;
import org.forum.main.services.interfaces.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LikeServiceImpl implements LikeService {

    private final LikeRepository repository;

    @Autowired
    public LikeServiceImpl(LikeRepository repository) {
        this.repository = repository;
    }

    @Override
    public Like findByMessageIdAndUserId(Long messageId, Integer userId) {
        return repository.findByMessageIdAndUserId(messageId, userId)
                .orElseThrow(() -> new ServiceException("Like with message id \"%s\" and user id \"%s\" doesn't exists"
                        .formatted(messageId, userId)));
    }

    @Override
    public void save(Message likedMessage, User userWhoLiked) {
        repository.save(
                Like.builder()
                        .message(likedMessage)
                        .user(userWhoLiked)
                        .creationDate(LocalDateTime.now())
                        .build()
        );
    }

    @Override
    public void cancel(Message likedMessage, User userWhoLiked) {
        repository.delete(findByMessageIdAndUserId(likedMessage.getId(), userWhoLiked.getId()));
    }

    @Override
    public void saveOrCancel(Message likedMessage, User userWhoLiked, boolean isCancellation) {
        if (!isCancellation) {
            save(likedMessage, userWhoLiked);
        } else {
            cancel(likedMessage, userWhoLiked);
        }
    }

}
