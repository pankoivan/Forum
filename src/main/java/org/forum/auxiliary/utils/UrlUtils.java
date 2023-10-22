package org.forum.auxiliary.utils;

import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

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

    public static String makeParametersString(Map<String, String[]> parameterMap) {
        if (parameterMap.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder("?");
        for (Map.Entry<String, String[]> kv : parameterMap.entrySet()) {
            sb.append("%s=%s".formatted(kv.getKey(), kv.getValue()[0])).append("&");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

}
