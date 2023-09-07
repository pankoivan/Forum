package org.forum.main.services.interfaces.common;

public interface SimpleSaveService<T, M> extends GeneralService<T, M> {

    void save(T t);

}
