package org.forum.main.services.interfaces;

import org.forum.main.entities.Message;
import org.forum.main.entities.User;

import java.util.List;

public interface StatisticsService {

    List<User> topUsers();

    List<Message> recentMessages();

    int usersCount();

    int usualUsersCount();

    int adminsCount();

    int modersCount();

    long messagesCount();

    long likesCount();

    int sectionsCount();

    int topicsCount();

    int bansCount();

    int currentBansCount();

}
