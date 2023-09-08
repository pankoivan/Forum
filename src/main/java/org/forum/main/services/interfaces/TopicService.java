package org.forum.main.services.interfaces;

import org.forum.auxiliary.sorting.SortingOption;
import org.forum.auxiliary.sorting.enums.TopicSortingProperties;
import org.forum.main.entities.Section;
import org.forum.main.entities.Topic;
import org.forum.main.services.interfaces.common.SortingService;
import org.forum.main.services.interfaces.common.ValidationService;
import org.forum.main.services.interfaces.common.GeneralService;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface TopicService extends GeneralService<Topic, Integer>, ValidationService<Topic>,
        SortingService<Topic, SortingOption<TopicSortingProperties>> {

    void save(Topic topic, Authentication authentication, Section section);

    List<Topic> findAllBySectionId(Integer sectionId);

}
