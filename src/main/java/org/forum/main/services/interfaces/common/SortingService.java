package org.forum.main.services.interfaces.common;

import java.util.List;

public interface SortingService<T, M> {

    M emptySortingOption();

    List<T> findAllSorted(M sortingOption);

    List<T> findAllSortedByDefault();

}
