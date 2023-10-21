package org.forum.auxiliary.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class SearchingUtils {

    public static boolean search(String sourceString, String searchedString) {
        String str1 = sourceString.toLowerCase(), str2 = searchedString.toLowerCase();
        return str1.contains(str2) || str2.contains(str1);
    }

}
