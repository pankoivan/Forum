package org.forum.main.services.interfaces.common;

public interface SimpleSaveService<T, M> extends CommonService<T, M> {

    void save(T t);

}
