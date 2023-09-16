package org.forum.main.services.implementations.common;

import java.util.List;

public abstract class AbstractPaginationServiceImpl<T> {

    public List<T> onPageImpl(List<T> foundItems, int pageNumber, int paginationConstant) {
        return foundItems.stream()
                .skip((long) (pageNumber - 1) * paginationConstant)
                .limit(paginationConstant)
                .toList();
    }

    public int pagesCountImpl(List<T> foundItems, int paginationConstant) {
        return !foundItems.isEmpty()
                ? Math.ceilDiv(foundItems.size(), paginationConstant)
                : 1;
    }

}
