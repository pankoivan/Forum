package org.forum.main.services.implementations;

import org.forum.auxiliary.exceptions.ServiceException;
import org.forum.main.entities.Like;
import org.forum.main.entities.Message;
import org.forum.main.entities.User;
import org.forum.main.repositories.DislikeRepository;
import org.forum.main.repositories.LikeRepository;
import org.forum.main.services.interfaces.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class LikeServiceImpl implements LikeService {

    private final LikeRepository repository;

    private final DislikeRepository dislikeRepository;

    @Autowired
    public LikeServiceImpl(LikeRepository repository, DislikeRepository dislikeRepository) {
        this.repository = repository;
        this.dislikeRepository = dislikeRepository;
    }

    @Override
    public Like findByMessageAndUser(Message likedMessage, User userWhoLiked) {
        return repository.findByMessageIdAndUserId(likedMessage.getId(), userWhoLiked.getId())
                .orElseThrow(
                        () -> new ServiceException("Like with message id \"%s\" and user id \"%s\" doesn't exists"
                                .formatted(likedMessage.getId(), userWhoLiked.getId()))
                );
    }

    @Override
    public void save(Message likedMessage, User userWhoLiked) {
        if (likedMessage.getUserWhoPosted().getId().equals(userWhoLiked.getId())) {
            return;
        }
        if (likedMessage.containsDislikedUserById(userWhoLiked.getId())) {
            dislikeRepository.delete(dislikeRepository.findByMessageIdAndUserId(likedMessage.getId(), userWhoLiked.getId())
                    .orElseThrow(
                            () -> new ServiceException("Dislike with message id \"%s\" and user id \"%s\" doesn't exists"
                                    .formatted(likedMessage.getId(), userWhoLiked.getId())))
            );
        }
        repository.save(
                Like.builder()
                        .message(likedMessage)
                        .user(userWhoLiked)
                        .creationDate(LocalDateTime.now())
                        .build()
        );
    }

    @Override
    public void delete(Message likedMessage, User userWhoLiked) {
        repository.delete(findByMessageAndUser(likedMessage, userWhoLiked));
    }

    @Override
    public void saveOrDelete(Message likedMessage, User userWhoLiked, boolean isCancellation) {
        if (!isCancellation) {
            save(likedMessage, userWhoLiked);
        } else {
            delete(likedMessage, userWhoLiked);
        }
    }

}
