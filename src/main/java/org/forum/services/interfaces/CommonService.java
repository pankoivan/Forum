package org.forum.services.interfaces;

import java.util.List;

public interface CommonService<T, M> {

    T empty();

    boolean isNew(T t);

    T findById(M id);

    List<T> findAll();

    void save(T t);

    void deleteById(M id);

}
