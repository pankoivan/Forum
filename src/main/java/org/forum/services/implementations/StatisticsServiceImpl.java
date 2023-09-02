package org.forum.services.implementations;

import org.forum.entities.Message;
import org.forum.entities.User;
import org.forum.repositories.*;
import org.forum.services.interfaces.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final SectionRepository sectionRepository;

    private final TopicRepository topicRepository;

    private final MessageRepository messageRepository;

    private final LikeRepository likeRepository;

    private final BanRepository banRepository;

    @Autowired
    public StatisticsServiceImpl(UserRepository userRepository, RoleRepository roleRepository,
                                 SectionRepository sectionRepository, TopicRepository topicRepository,
                                 MessageRepository messageRepository, LikeRepository likeRepository,
                                 BanRepository banRepository) {

        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.sectionRepository = sectionRepository;
        this.topicRepository = topicRepository;
        this.messageRepository = messageRepository;
        this.likeRepository = likeRepository;
        this.banRepository = banRepository;
    }

    @Override
    public List<User> topUsers() {
        return userRepository.findAll().stream().limit(10).toList();
    }

    @Override
    public List<Message> recentMessages() {
        return messageRepository.findAll().stream().limit(5).toList();
    }

    @Override
    public int usersCount() {
        return userRepository.findAll().size();
    }

    @Override
    public int usualUsersCount() {
        return countByRoleName("ROLE_USER");
    }

    @Override
    public int adminsCount() {
        return countByRoleName("ROLE_ADMIN");
    }

    @Override
    public int modersCount() {
        return countByRoleName("ROLE_MODER");
    }

    @Override
    public long messagesCount() {
        return messageRepository.findAll().size();
    }

    @Override
    public long likesCount() {
        return likeRepository.findAll().size();
    }

    @Override
    public int sectionsCount() {
        return sectionRepository.findAll().size();
    }

    @Override
    public int topicsCount() {
        return topicRepository.findAll().size();
    }

    @Override
    public int bansCount() {
        return banRepository.findAll().size();
    }

    @Override
    public int currentBansCount() {
        return banRepository.findAllByEndDateAfter(LocalDate.now()).size();
    }

    private int countByRoleName(String roleName) {
        return userRepository.findAllByRoleId(roleRepository.findByName(roleName)
                        .orElseThrow(() -> new RuntimeException("Role with name \"" + roleName + "\" doesn't exists"))
                        .getId())
                .size();
    }

}