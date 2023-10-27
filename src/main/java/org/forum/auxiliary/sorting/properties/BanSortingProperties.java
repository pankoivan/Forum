package org.forum.auxiliary.sorting.properties;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum BanSortingProperties {

    BY_BANNED_USER_NICKNAME("По нику забаненного"),

    BY_START_DATE("По дате начала"),

    BY_END_DATE("По дате окончания"),

    BY_REASON("По причине"),

    BY_AUTHOR_NICKNAME("По нику забанившего");

    private final String alias;

}
