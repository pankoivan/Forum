package org.forum.main.services.interfaces;

import org.forum.auxiliary.sorting.SortingOption;
import org.forum.auxiliary.sorting.enums.SectionSortingProperties;
import org.forum.main.entities.Section;
import org.forum.main.services.interfaces.common.GeneralService;
import org.forum.main.services.interfaces.common.SortingService;
import org.forum.main.services.interfaces.common.ValidationService;
import org.springframework.security.core.Authentication;

public interface SectionService extends GeneralService<Section, Integer>, ValidationService<Section>,
        SortingService<Section, SortingOption<SectionSortingProperties>> {

    void save(Section section, Authentication authentication);

}
