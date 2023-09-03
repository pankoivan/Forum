package org.forum.global.comparators;

import org.forum.entities.interfaces.ChronoGetter;

import java.time.LocalDateTime;
import java.util.Comparator;

public class LocalDateTimeComparator implements Comparator<ChronoGetter<LocalDateTime>> {

    @Override
    public int compare(ChronoGetter<LocalDateTime> chrono1, ChronoGetter<LocalDateTime> chrono2) {
        return chrono1.get().isBefore(chrono2.get()) ? -1
                : chrono1.get().isAfter(chrono2.get()) ? 1
                : 0;
    }

}
