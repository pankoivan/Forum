package org.forum.main.services.interfaces;

import org.forum.main.entities.Ban;
import org.forum.main.entities.User;
import org.forum.main.services.interfaces.common.ValidationService;

public interface BanService extends ValidationService<Ban> {

    Ban empty();

    void save(Ban ban, User user, User userWhoAssigned);

    void unban(User user);

}
