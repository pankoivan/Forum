package org.forum.auxiliary.sorting.properties;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum UserSortingProperties {

    BY_NICKNAME("По никнейму"),

    BY_REGISTRATION_DATE("По дате регистрации"),

    BY_MESSAGES_COUNT("По количеству сообщений"),

    BY_REPUTATION("По репутации");

    private final String alias;

}
