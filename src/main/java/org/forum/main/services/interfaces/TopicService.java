package org.forum.main.services.interfaces;

import org.forum.auxiliary.sorting.options.TopicSortingOption;
import org.forum.main.entities.Section;
import org.forum.main.entities.Topic;
import org.forum.main.services.interfaces.common.*;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface TopicService extends GeneralService<Topic, Integer>, ValidationService<Topic>,
        PaginationService<Topic>, SortingService<Topic, TopicSortingOption>, SearchingService<Topic> {

    void save(Topic topic, Authentication authentication, Section section);

    List<Topic> findAllBySectionId(Integer sectionId);

    List<Topic> findAllBySectionIdSorted(Integer sectionId, TopicSortingOption sortingOption);

    List<Topic> findAllBySectionIdSorted(Integer sectionId);

    List<Topic> findAllByUserIdSorted(Integer userId, TopicSortingOption sortingOption);

    List<Topic> findAllByUserIdSorted(Integer userId);

}
