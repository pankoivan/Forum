package org.forum.main.services.interfaces;

import org.forum.auxiliary.sorting.options.SectionSortingOption;
import org.forum.main.entities.Section;
import org.forum.main.entities.User;
import org.forum.main.services.interfaces.common.*;

import java.util.List;

public interface SectionService extends GeneralService<Section, Integer>, ValidationService<Section>,
        PaginationService<Section>, SortingService<Section, SectionSortingOption>, SearchingService<Section> {

    void save(Section section, User author);

    List<Section> findAllByUserIdSorted(Integer userId, SectionSortingOption sortingOption);

    List<Section> findAllByUserIdSorted(Integer userId);

}
