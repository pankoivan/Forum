package org.forum.global.constants;

import lombok.experimental.UtilityClass;

import java.time.format.DateTimeFormatter;

@UtilityClass
public final class DateTimeFormatConstants {

    public static final DateTimeFormatter SEPARATED_FORMAT =
            DateTimeFormatter.ofPattern("dd-MM-yyyy в HH:mm:ss");

}
