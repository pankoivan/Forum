package org.forum.main.entities.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum Gender {

    MALE("Мужской"),

    FEMALE("Женский");

    private final String alias;

}
