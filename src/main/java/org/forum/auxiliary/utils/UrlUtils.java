package org.forum.auxiliary.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class UrlUtils {

    public static String addFirstSlash(String url) {
        return "/" + url;
    }

    public static String removeFirstSlash(String url) {
        return url.replaceFirst("/", "");
    }

}
