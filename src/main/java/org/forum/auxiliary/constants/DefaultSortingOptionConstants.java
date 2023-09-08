package org.forum.auxiliary.constants;

import lombok.experimental.UtilityClass;
import org.forum.auxiliary.sorting.SortingOption;
import org.forum.auxiliary.sorting.SortingOptionImpl;
import org.forum.auxiliary.sorting.enums.MessageSortingProperties;
import org.forum.auxiliary.sorting.enums.SectionSortingProperties;
import org.forum.auxiliary.sorting.enums.TopicSortingProperties;
import org.forum.auxiliary.sorting.enums.UserSortingProperties;
import org.springframework.data.domain.Sort;

@UtilityClass
public final class DefaultSortingOptionConstants {

    public static final SortingOption<UserSortingProperties> FOR_USERS =
            new SortingOptionImpl<>(Sort.Direction.DESC, UserSortingProperties.BY_LIKES_COUNT);

    public static final SortingOption<SectionSortingProperties> FOR_SECTIONS =
            new SortingOptionImpl<>(Sort.Direction.DESC, SectionSortingProperties.BY_MESSAGES_COUNT);

    public static final SortingOption<TopicSortingProperties> FOR_TOPICS =
            new SortingOptionImpl<>(Sort.Direction.DESC, TopicSortingProperties.BY_MESSAGES_COUNT);

    public static final SortingOption<MessageSortingProperties> FOR_MESSAGES =
            new SortingOptionImpl<>(Sort.Direction.DESC, MessageSortingProperties.BY_LIKES_COUNT);

}
