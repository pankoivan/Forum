package org.forum.auxiliary.utils;

import lombok.experimental.UtilityClass;
import org.forum.auxiliary.exceptions.PathVariableUtilsException;

import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@UtilityClass
public final class PathVariableUtils {

    private static final String PATH_VARIABLE_PATTERN = "\\{.*?}";

    public static Integer toNonNegativeInteger(String pathVariable) throws PathVariableUtilsException {
        return forNonNegativeIntegerAndLong(() -> Integer.parseInt(pathVariable), pathVariable, "integer");
    }

    public static Long toNonNegativeLong(String pathVariable) throws PathVariableUtilsException {
        return forNonNegativeIntegerAndLong(() -> Long.parseLong(pathVariable), pathVariable, "long");
    }

    private static <T extends Number> T forNonNegativeIntegerAndLong(Supplier<T> supplier, String pathVariable,
                                                                     String exceptionMessagePart)
            throws PathVariableUtilsException {

        T result;
        try {
            result = supplier.get();
        } catch (NumberFormatException e) {
            throw new PathVariableUtilsException("Path variable \"" + pathVariable + "\" is not an non-negative " +
                    exceptionMessagePart);
        }
        if (result.doubleValue() < 0) {
            throw new PathVariableUtilsException("Path variable \"" + pathVariable + "\" is not an non-negative " +
                    exceptionMessagePart);
        }
        return result;
    }

    public static String replacePatternPart(String sourceUrl, Object replacementPart) {
        return sourceUrl.replaceFirst(PATH_VARIABLE_PATTERN, replacementPart.toString());
    }

    public static String replacePatternParts(String sourceUrl, Object ... replacementParts)
            throws PathVariableUtilsException {

        Matcher matcher = Pattern.compile(PATH_VARIABLE_PATTERN).matcher(sourceUrl);
        for (int i = 0; matcher.find(); ++i) {
            try {
                sourceUrl = replacePatternPart(sourceUrl, replacementParts[i]);
            } catch (ArrayIndexOutOfBoundsException e) {
                throw new PathVariableUtilsException(
                        "Number of pattern parts in string \"%s\" greater then number of replacement parts (%s)"
                                .formatted(sourceUrl, replacementParts.length),
                        e
                );
            }
        }
        System.out.println("Replaced: " + sourceUrl);
        return sourceUrl;
    }

}
