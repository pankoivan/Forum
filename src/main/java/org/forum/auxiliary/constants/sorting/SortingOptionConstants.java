package org.forum.auxiliary.constants.sorting;

import lombok.experimental.UtilityClass;
import org.forum.auxiliary.sorting.options.MessageSortingOption;
import org.forum.auxiliary.sorting.options.SectionSortingOption;
import org.forum.auxiliary.sorting.options.TopicSortingOption;
import org.forum.auxiliary.sorting.properties.MessageSortingProperties;
import org.forum.auxiliary.sorting.properties.SectionSortingProperties;
import org.forum.auxiliary.sorting.properties.TopicSortingProperties;
import org.springframework.data.domain.Sort;

@UtilityClass
public class SortingOptionConstants {

    public static final SectionSortingOption SECTIONS_BY_CREATION_DATE_ASC =
            new SectionSortingOption(Sort.Direction.ASC, SectionSortingProperties.BY_CREATION_DATE);

    public static final TopicSortingOption TOPICS_BY_CREATION_DATE_ASC =
            new TopicSortingOption(Sort.Direction.ASC, TopicSortingProperties.BY_CREATION_DATE);

    public static final MessageSortingOption MESSAGES_BY_CREATION_DATE_ASC =
            new MessageSortingOption(Sort.Direction.ASC, MessageSortingProperties.BY_CREATION_DATE);

}
