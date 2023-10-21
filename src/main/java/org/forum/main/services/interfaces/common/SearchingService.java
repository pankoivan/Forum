package org.forum.main.services.interfaces.common;

import java.util.List;

public interface SearchingService<T> {

    List<T> search(List<T> sourceList, String searchedString);

}
