package org.forum.utils;

import lombok.experimental.UtilityClass;
import org.forum.entities.User;

import java.security.Principal;

@UtilityClass
public class PrincipalUtils {

    public static User extractCurrentUserOrNull(Principal principal) {
        return principal instanceof User user
                ? user
                : null;
    }

}
