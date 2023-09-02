package org.forum.services.interfaces;

import org.forum.entities.Message;
import org.forum.entities.User;

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
