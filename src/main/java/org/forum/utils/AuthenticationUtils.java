package org.forum.utils;

import lombok.experimental.UtilityClass;
import org.forum.entities.User;
import org.springframework.security.core.Authentication;

@UtilityClass
public class AuthenticationUtils {

    public static User extractCurrentUserOrNull(Authentication authentication) {
        if (authentication == null) {
            return null;
        }
        if (authentication.getPrincipal() instanceof User user) {
            return user;
        }
        throw new RuntimeException("Security user (i.e. principal) is not an entity user");
    }

}
