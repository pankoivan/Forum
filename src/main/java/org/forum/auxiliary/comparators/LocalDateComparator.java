package org.forum.auxiliary.comparators;

import org.forum.main.entities.interfaces.ChronoGetter;

import java.time.LocalDate;
import java.util.Comparator;

public class LocalDateComparator implements Comparator<ChronoGetter<LocalDate>> {

    @Override
    public int compare(ChronoGetter<LocalDate> chrono1, ChronoGetter<LocalDate> chrono2) {
        return chrono1.get().isBefore(chrono2.get()) ? -1
                : chrono1.get().isAfter(chrono2.get()) ? 1
                : 0;
    }

}
