package org.forum.main.services.implementations;

import org.forum.auxiliary.exceptions.ServiceException;
import org.forum.main.entities.Dislike;
import org.forum.main.entities.Message;
import org.forum.main.entities.User;
import org.forum.main.repositories.DislikeRepository;
import org.forum.main.services.interfaces.DislikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DislikeServiceImpl implements DislikeService {

    private final DislikeRepository repository;

    @Autowired
    public DislikeServiceImpl(DislikeRepository repository) {
        this.repository = repository;
    }

    @Override
    public Dislike findByMessageIdAndUserId(Long messageId, Integer userId) {
        return repository.findByMessageIdAndUserId(messageId, userId)
                .orElseThrow(() -> new ServiceException("Dislike with message id \"%s\" and user id \"%s\" doesn't exists"
                        .formatted(messageId, userId)));
    }

    @Override
    public void save(Message dislikedMessage, User userWhoDisliked) {
        repository.save(
                Dislike.builder()
                        .message(dislikedMessage)
                        .user(userWhoDisliked)
                        .creationDate(LocalDateTime.now())
                        .build()
        );
    }

    @Override
    public void cancel(Message dislikedMessage, User userWhoDisliked) {
        repository.delete(findByMessageIdAndUserId(dislikedMessage.getId(), userWhoDisliked.getId()));
    }

    @Override
    public void saveOrCancel(Message dislikedMessage, User userWhoDisliked, boolean isCancellation) {
        if (!isCancellation) {
            save(dislikedMessage, userWhoDisliked);
        } else {
            cancel(dislikedMessage, userWhoDisliked);
        }
    }

}
