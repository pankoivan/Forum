package org.forum.auxiliary.sorting.properties;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum TopicSortingProperties {

    BY_NAME("По названию"),

    BY_CREATION_DATE("По дате создания"),

    BY_MESSAGES_COUNT("По количеству сообщений");

    private final String alias;

}
