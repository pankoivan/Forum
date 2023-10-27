package org.forum.main.services.implementations;

import org.forum.auxiliary.exceptions.ServiceException;
import org.forum.main.entities.Dislike;
import org.forum.main.entities.Message;
import org.forum.main.entities.User;
import org.forum.main.repositories.DislikeRepository;
import org.forum.main.repositories.LikeRepository;
import org.forum.main.services.interfaces.DislikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DislikeServiceImpl implements DislikeService {

    private final DislikeRepository repository;

    private final LikeRepository likeRepository;

    @Autowired
    public DislikeServiceImpl(DislikeRepository repository, LikeRepository likeRepository) {
        this.repository = repository;
        this.likeRepository = likeRepository;
    }

    @Override
    public Dislike findByMessageAndUser(Message dislikedMessage, User userWhoDisliked) {
        return repository.findByMessageIdAndUserId(dislikedMessage.getId(), userWhoDisliked.getId())
                .orElseThrow(
                        () -> new ServiceException("Dislike with message id \"%s\" and user id \"%s\" doesn't exists"
                                .formatted(dislikedMessage.getId(), userWhoDisliked.getId()))
                );
    }

    @Override
    public void save(Message dislikedMessage, User userWhoDisliked) {
        if (dislikedMessage.getUserWhoPosted().getId().equals(userWhoDisliked.getId())) {
            return;
        }
        if (dislikedMessage.containsLikedUserById(userWhoDisliked.getId())) {
            likeRepository.delete(likeRepository.findByMessageIdAndUserId(dislikedMessage.getId(), userWhoDisliked.getId())
                    .orElseThrow(
                            () -> new ServiceException("Like with message id \"%s\" and user id \"%s\" doesn't exists"
                                    .formatted(dislikedMessage.getId(), userWhoDisliked.getId())))
            );
        }
        repository.save(
                Dislike.builder()
                        .message(dislikedMessage)
                        .user(userWhoDisliked)
                        .creationDate(LocalDateTime.now())
                        .build()
        );
    }

    @Override
    public void delete(Message dislikedMessage, User userWhoDisliked) {
        repository.delete(findByMessageAndUser(dislikedMessage, userWhoDisliked));
    }

    @Override
    public void saveOrDelete(Message dislikedMessage, User userWhoDisliked, boolean isCancellation) {
        if (!isCancellation) {
            save(dislikedMessage, userWhoDisliked);
        } else {
            delete(dislikedMessage, userWhoDisliked);
        }
    }

}
