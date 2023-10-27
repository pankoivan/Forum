package org.forum.auxiliary.utils;

import lombok.experimental.UtilityClass;

@UtilityClass
public final class SearchingUtils {

    public static boolean search(String sourceString, String searchedString) {
        String source = sourceString.toLowerCase(), searched = searchedString.toLowerCase();
        return source.contains(searched) || searched.contains(source);
    }

    public static boolean search(String searchedString, String ... sourceStrings) {
        String searched = searchedString.toLowerCase();
        for (String source : sourceStrings) {
            return searched.contains(source) || source.contains(searched);
        }
        return false;
    }

}
