package org.forum.services.interfaces.common;

import java.util.List;

public interface PaginationService<T> {

    List<T> extractForPage(List<T> foundItems, int pageNumber);

    int calculatePagesCount(List<T> items);

}
