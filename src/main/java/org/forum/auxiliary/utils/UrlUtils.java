package org.forum.auxiliary.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class UrlUtils {

    public static String concat(String ... urlParts) {
        return String.join("/", urlParts);
    }

    public static String concatWithStartSlash(String ... urlParts) {
        return "/" + concat(urlParts);
    }

    public static String addStartSlash(String url) {
        return "/" + url;
    }

    public static String removeStartSlash(String url) {
        return url.replaceFirst("/", "");
    }

    public static String removePage(String url) {
        return url.contains("/page")
                ? url.substring(0, url.lastIndexOf("/"))
                : url;
    }

}
