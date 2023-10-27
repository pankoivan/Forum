package org.forum.main.services.interfaces;

import org.forum.main.entities.Ban;
import org.forum.main.entities.User;
import org.forum.main.services.interfaces.common.PaginationService;
import org.forum.main.services.interfaces.common.ValidationService;

import java.util.List;

public interface BanService extends ValidationService<Ban>, PaginationService<Ban> {

    Ban empty();

    List<Ban> findAllByUserId(Integer id);

    List<Ban> findAllByUserWhoAssignedId(Integer id);

    void save(Ban ban, User user, User userWhoAssigned);

    void unban(User user);

}
