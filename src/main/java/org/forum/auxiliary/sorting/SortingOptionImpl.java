package org.forum.auxiliary.sorting;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Sort;

@NoArgsConstructor
@AllArgsConstructor
@Setter
public class SortingOptionImpl<T extends Enum<T>> implements SortingOption<T> {

    private Sort.Direction direction;

    private T property;

    @Override
    public Sort.Direction getDirection() {
        return direction;
    }

    @Override
    public T getProperty() {
        return property;
    }

}
