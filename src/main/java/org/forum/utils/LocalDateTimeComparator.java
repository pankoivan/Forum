package org.forum.utils;

import org.forum.entities.interfaces.LocalDateTimeGetter;

import java.util.Comparator;

public class LocalDateTimeComparator implements Comparator<LocalDateTimeGetter> {

    @Override
    public int compare(LocalDateTimeGetter chrono1, LocalDateTimeGetter chrono2) {
        return chrono1.get().isBefore(chrono2.get()) ? -1
                : chrono1.get().isAfter(chrono2.get()) ? 1
                : 0;
    }

}
