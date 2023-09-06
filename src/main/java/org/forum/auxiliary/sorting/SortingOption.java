package org.forum.auxiliary.sorting;

import org.springframework.data.domain.Sort;

public interface SortingOption<T extends Enum<T>> {

    T getProperty();

    Sort.Direction getDirection();

}
