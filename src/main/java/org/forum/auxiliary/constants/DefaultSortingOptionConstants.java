package org.forum.auxiliary.constants;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.forum.auxiliary.sorting.SortingOption;
import org.forum.auxiliary.sorting.SortingOptionImpl;
import org.forum.auxiliary.sorting.enums.MessageSortingProperties;
import org.forum.auxiliary.sorting.enums.SectionSortingProperties;
import org.forum.auxiliary.sorting.enums.TopicSortingProperties;
import org.forum.auxiliary.sorting.enums.UserSortingProperties;
import org.springframework.data.domain.Sort;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum DefaultSortingOptionConstants {

    FOR_USERS(
            new SortingOptionImpl<>(Sort.Direction.ASC, UserSortingProperties.BY_NICKNAME)
    ),

    FOR_SECTIONS(
            new SortingOptionImpl<>(Sort.Direction.ASC, SectionSortingProperties.BY_NAME)
    ),

    FOR_TOPICS(
            new SortingOptionImpl<>(Sort.Direction.ASC, TopicSortingProperties.BY_NAME)
    ),

    FOR_MESSAGES(
            new SortingOptionImpl<>(Sort.Direction.ASC, MessageSortingProperties.BY_CREATION_DATE)
    );

    private final SortingOption<? extends Enum<?>> value;

}
