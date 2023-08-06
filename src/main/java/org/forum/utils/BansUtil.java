package org.forum.utils;

import lombok.experimental.UtilityClass;
import org.forum.entities.User;

import java.time.LocalDate;

@UtilityClass
public final class BansUtil {

    public static boolean isBanned(User user) {
        return user.getBans()
                .stream()
                .anyMatch(ban -> LocalDate.now().isBefore(ban.getEndDate()));
    }

}
