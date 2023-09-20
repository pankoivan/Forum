package org.forum.auxiliary.utils;

import lombok.experimental.UtilityClass;
import org.forum.auxiliary.exceptions.PathVariableUtilsException;

import java.util.function.Supplier;

@UtilityClass
public final class PathVariableUtils {

    public static Integer toNonNegativeInteger(String pathVariable) throws PathVariableUtilsException {
        return forNonNegativeIntegerAndLong(() -> Integer.parseInt(pathVariable), pathVariable, "integer");
    }

    public static Long toNonNegativeLong(String pathVariable) throws PathVariableUtilsException {
        return forNonNegativeIntegerAndLong(() -> Long.parseLong(pathVariable), pathVariable, "long");
    }

    private static <T extends Number> T forNonNegativeIntegerAndLong(Supplier<T> supplier, String pathVariable,
                                                                     String exceptionMessagePart) throws PathVariableUtilsException {
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

}
