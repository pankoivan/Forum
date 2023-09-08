package org.forum.auxiliary.sorting;

import org.springframework.data.domain.Sort;

public interface SortingOption<T extends Enum<T>> {

    Sort.Direction getDirection();

    T getProperty();

}
