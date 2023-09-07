package org.forum.main.services.interfaces.common;

import java.util.List;

public interface SortingService<T, M> {

    List<T> findAllSorted(M sortingOption);

}
