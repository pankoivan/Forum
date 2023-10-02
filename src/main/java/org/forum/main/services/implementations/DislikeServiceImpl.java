package org.forum.main.services.implementations;

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
    public void save(Message dislikedMessage, User userWhoDisliked) {
        repository.save(
                Dislike.builder()
                        .message(dislikedMessage)
                        .user(userWhoDisliked)
                        .creationDate(LocalDateTime.now())
                        .build()
        );
    }

}
