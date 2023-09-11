package org.forum.auxiliary.sorting.options;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.forum.auxiliary.sorting.SortingOption;
import org.forum.auxiliary.sorting.enums.SectionSortingProperties;
import org.springframework.data.domain.Sort;

@NoArgsConstructor
@AllArgsConstructor
@Setter
public class SectionSortingOption implements SortingOption<SectionSortingProperties> {

    private Sort.Direction direction;

    private SectionSortingProperties property;

    @Override
    public Sort.Direction getDirection() {
        return direction;
    }

    @Override
    public SectionSortingProperties getProperty() {
        return property;
    }

}
