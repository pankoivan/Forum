package org.forum.main.services.interfaces.common;

import java.util.List;

public interface CommonService<T, M> {

    T empty();

    boolean isNew(T t);

    T findById(M id);

    List<T> findAll();

    void deleteById(M id);

}
