package org.forum.auxiliary.constants;

import lombok.experimental.UtilityClass;

import java.time.format.DateTimeFormatter;

@UtilityClass
public final class DateTimeFormatConstants {

    public static final DateTimeFormatter SEPARATED_DATE_TIME =
            DateTimeFormatter.ofPattern("dd-MM-yyyy в HH:mm:ss");

}
