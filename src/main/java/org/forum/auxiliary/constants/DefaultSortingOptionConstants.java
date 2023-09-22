package org.forum.auxiliary.constants;

import lombok.experimental.UtilityClass;
import org.forum.auxiliary.sorting.enums.MessageSortingProperties;
import org.forum.auxiliary.sorting.enums.SectionSortingProperties;
import org.forum.auxiliary.sorting.enums.TopicSortingProperties;
import org.forum.auxiliary.sorting.enums.UserSortingProperties;
import org.forum.auxiliary.sorting.options.MessageSortingOption;
import org.forum.auxiliary.sorting.options.SectionSortingOption;
import org.forum.auxiliary.sorting.options.TopicSortingOption;
import org.forum.auxiliary.sorting.options.UserSortingOption;
import org.springframework.data.domain.Sort;

@UtilityClass
public final class DefaultSortingOptionConstants {

    public static final UserSortingOption FOR_USERS =
            new UserSortingOption(Sort.Direction.ASC, UserSortingProperties.BY_NICKNAME);

    public static final SectionSortingOption FOR_SECTIONS =
            new SectionSortingOption(Sort.Direction.ASC, SectionSortingProperties.BY_NAME);

    public static final TopicSortingOption FOR_TOPICS =
            new TopicSortingOption(Sort.Direction.ASC, TopicSortingProperties.BY_NAME);

    public static final MessageSortingOption FOR_MESSAGES =
            new MessageSortingOption(Sort.Direction.ASC, MessageSortingProperties.BY_CREATION_DATE);

}
