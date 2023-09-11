package org.forum.main.services.interfaces.common;

import org.forum.auxiliary.sorting.SortingOption;

import java.util.List;

public interface SortingService<T, M extends SortingOption<? extends Enum<?>>> {

    M emptySortingOption();

    List<T> findAllSorted(M sortingOption);

    List<T> findAllSortedByDefault();

}
