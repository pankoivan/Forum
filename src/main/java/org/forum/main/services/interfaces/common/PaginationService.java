package org.forum.main.services.interfaces.common;

import java.util.List;

public interface PaginationService<T> {

    List<T> onPage(List<T> foundItems, int pageNumber);

    int pagesCount(List<T> foundItems);

}
