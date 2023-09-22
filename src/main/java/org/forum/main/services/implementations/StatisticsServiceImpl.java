package org.forum.main.services.implementations;

import org.forum.main.entities.Message;
import org.forum.main.entities.User;
import org.forum.main.repositories.*;
import org.forum.main.services.interfaces.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    private final UserRepository userRepository;

    private final SectionRepository sectionRepository;

    private final TopicRepository topicRepository;

    private final MessageRepository messageRepository;

    private final LikeRepository likeRepository;

    private final DislikeRepository dislikeRepository;

    private final BanRepository banRepository;

    @Autowired
    public StatisticsServiceImpl(UserRepository userRepository, SectionRepository sectionRepository,
                                 TopicRepository topicRepository, MessageRepository messageRepository,
                                 LikeRepository likeRepository, DislikeRepository dislikeRepository,
                                 BanRepository banRepository) {
        this.userRepository = userRepository;
        this.sectionRepository = sectionRepository;
        this.topicRepository = topicRepository;
        this.messageRepository = messageRepository;
        this.likeRepository = likeRepository;
        this.dislikeRepository = dislikeRepository;
        this.banRepository = banRepository;
    }

    @Override
    public List<User> topUsers() {
        return userRepository.findAllByOrderByLikesCountWithDirection("DESC");
    }

    @Override
    public List<Message> recentMessages() {
        return messageRepository.findAll(Sort.by(Sort.Direction.DESC, "creationDate"))
                .stream()
                .limit(5)
                .toList();
    }

    @Override
    public int usersCount() {
        return (int) userRepository.count();
    }

    @Override
    public int usualUsersCount() {
        return userRepository.findAllByRoleName("ROLE_USER").size();
    }

    @Override
    public int modersCount() {
        return userRepository.findAllByRoleName("ROLE_MODER").size();
    }

    @Override
    public int adminsCount() {
        return userRepository.findAllByRoleName("ROLE_ADMIN").size();
    }

    @Override
    public int sectionsCount() {
        return (int) sectionRepository.count();
    }

    @Override
    public int topicsCount() {
        return (int) topicRepository.count();
    }

    @Override
    public long messagesCount() {
        return messageRepository.count();
    }

    @Override
    public long likesCount() {
        return likeRepository.count();
    }

    @Override
    public long dislikesCount() {
        return dislikeRepository.count();
    }

    @Override
    public int bansCount() {
        return (int) banRepository.count();
    }

    @Override
    public int currentBansCount() {
        return banRepository.findAllByEndDateAfter(LocalDate.now()).size();
    }

}
