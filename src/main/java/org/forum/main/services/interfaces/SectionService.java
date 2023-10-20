package org.forum.main.services.interfaces;

import org.forum.auxiliary.sorting.options.SectionSortingOption;
import org.forum.main.entities.Section;
import org.forum.main.entities.Topic;
import org.forum.main.services.interfaces.common.GeneralService;
import org.forum.main.services.interfaces.common.PaginationService;
import org.forum.main.services.interfaces.common.SortingService;
import org.forum.main.services.interfaces.common.ValidationService;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface SectionService extends GeneralService<Section, Integer>, ValidationService<Section>,
        PaginationService<Section>, SortingService<Section, SectionSortingOption> {

    void save(Section section, Authentication authentication);

    List<Section> findAllByUserIdSorted(Integer userId, SectionSortingOption sortingOption);

    List<Section> findAllByUserIdSorted(Integer userId);

}
