package org.forum.utils;

import org.forum.entities.interfaces.LocalDateGetter;

import java.util.Comparator;

public class LocalDateComparator implements Comparator<LocalDateGetter> {

    @Override
    public int compare(LocalDateGetter chrono1, LocalDateGetter chrono2) {
        return chrono1.get().isBefore(chrono2.get()) ? -1
                : chrono1.get().isAfter(chrono2.get()) ? 1
                : 0;
    }

}
