package org.forum.auxiliary.utils;

import lombok.experimental.UtilityClass;
import org.forum.auxiliary.constants.url.UrlPartConstants;

import java.util.Map;

@UtilityClass
public final class UrlUtils {

    private static final String PAGINATION_PATTERN = "/" + UrlPartConstants.PAGE + "\\d*";

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

    public static String removePagination(String url) {
        return url.replaceFirst(PAGINATION_PATTERN, "");
    }

    private static String makeParameter(Map.Entry<String, String[]> parameter) {
        StringBuilder sb = new StringBuilder();
        for (String value : parameter.getValue()) {
            sb.append("%s=%s".formatted(parameter.getKey(), value)).append("&");
        }
        return sb.deleteCharAt(sb.length() - 1).toString();
    }

    public static String makeParametersString(Map<String, String[]> parameterMap) {
        if (parameterMap.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder("?");
        for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
            sb.append(makeParameter(entry)).append("&");
        }
        return sb.deleteCharAt(sb.length() - 1).toString();
    }

}
