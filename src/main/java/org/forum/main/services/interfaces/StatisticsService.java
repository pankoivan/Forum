package org.forum.main.services.interfaces;

import org.forum.main.entities.Message;
import org.forum.main.entities.User;

import java.util.List;

public interface StatisticsService {

    List<User> topUsers();

    List<Message> recentMessages();

    int usersCount();

    int usualUsersCount();

    int modersCount();

    int adminsCount();

    int sectionsCount();

    int topicsCount();

    long messagesCount();

    long likesCount();

    long dislikesCount();

    int bansCount();

    int currentBansCount();

}
