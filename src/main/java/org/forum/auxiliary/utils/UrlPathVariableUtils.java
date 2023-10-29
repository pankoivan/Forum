package org.forum.auxiliary.utils;

import lombok.experimental.UtilityClass;
import org.forum.auxiliary.exceptions.UrlPathVariableUtilsException;

import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public final class UrlPathVariableUtils {

    private static final String PATH_VARIABLE_PATTERN = "\\{.*?}";

    public static int toNonNegativeInteger(String pathVariable) throws UrlPathVariableUtilsException {
        return forNonNegativeIntegerAndLong(() -> Integer.parseInt(pathVariable), pathVariable, "integer");
    }

    public static long toNonNegativeLong(String pathVariable) throws UrlPathVariableUtilsException {
        return forNonNegativeIntegerAndLong(() -> Long.parseLong(pathVariable), pathVariable, "long");
    }

    private static <T extends Number> T forNonNegativeIntegerAndLong(Supplier<T> supplier, String pathVariable,
                                                                     String exceptionMessagePart)
            throws UrlPathVariableUtilsException {

        T result;
        try {
            result = supplier.get();
        } catch (NumberFormatException e) {
            throw new UrlPathVariableUtilsException("Path variable \"%s\" is not an non-negative %s"
                    .formatted(pathVariable, exceptionMessagePart));
        }
        if (result.doubleValue() < 0) {
            throw new UrlPathVariableUtilsException("Path variable \"%s\" is not an non-negative %s"
                    .formatted(pathVariable, exceptionMessagePart));
        }
        return result;
    }

    public static String replacePatternPart(String sourceUrl, String replacementPart) {
        return sourceUrl.replaceFirst(PATH_VARIABLE_PATTERN, replacementPart);
    }

    public static String replacePatternParts(String sourceUrl, String ... replacementParts) throws UrlPathVariableUtilsException {

        Matcher matcher = Pattern.compile(PATH_VARIABLE_PATTERN).matcher(sourceUrl);
        for (int i = 0; matcher.find(); ++i) {
            try {
                sourceUrl = replacePatternPart(sourceUrl, replacementParts[i]);
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new UrlPathVariableUtilsException(
                        "Number of pattern parts in string \"%s\" greater then number of replacement parts: (%s)"
                                .formatted(sourceUrl, replacementParts.length),
                        e
                );
            }
        }
        return sourceUrl;
    }

}
