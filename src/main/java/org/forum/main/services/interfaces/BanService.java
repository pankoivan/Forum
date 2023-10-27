package org.forum.main.services.interfaces;

import org.forum.auxiliary.sorting.options.BanSortingOption;
import org.forum.main.entities.Ban;
import org.forum.main.entities.User;
import org.forum.main.services.interfaces.common.PaginationService;
import org.forum.main.services.interfaces.common.SearchingService;
import org.forum.main.services.interfaces.common.SortingService;
import org.forum.main.services.interfaces.common.ValidationService;

import java.util.List;

public interface BanService extends ValidationService<Ban>, PaginationService<Ban>, SortingService<Ban, BanSortingOption>,
        SearchingService<Ban> {

    void save(Ban ban, User user, User userWhoAssigned);

    void unban(User user);

    Ban empty();

    List<Ban> findAll();

    List<Ban> findAllByUserId(Integer id);

    List<Ban> findAllByUserIdSorted(Integer id, BanSortingOption sortingOption);

    List<Ban> findAllByUserIdSorted(Integer id);

    List<Ban> findAllByUserWhoAssignedId(Integer id);

    List<Ban> findAllByUserWhoAssignedIdSorted(Integer id, BanSortingOption sortingOption);

    List<Ban> findAllByUserWhoAssignedIdSorted(Integer id);

}
