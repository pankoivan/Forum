package org.forum.auxiliary.sorting.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum MessageSortingProperties {

    BY_CREATION_DATE("По дате создания"),

    BY_EDITING_DATE("По дате редактирования"),

    BY_LIKES_COUNT("По количеству лайков"),

    BY_DISLIKES_COUNT("По количеству дизлайков");

    private final String alias;

}
