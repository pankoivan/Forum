package org.forum.auxiliary.sorting.options;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.forum.auxiliary.sorting.SortingOption;
import org.forum.auxiliary.sorting.enums.TopicSortingProperties;
import org.springframework.data.domain.Sort;

@NoArgsConstructor
@AllArgsConstructor
@Setter
public class TopicSortingOption implements SortingOption<TopicSortingProperties> {

    private Sort.Direction direction;

    private TopicSortingProperties property;

    @Override
    public Sort.Direction getDirection() {
        return direction;
    }

    @Override
    public TopicSortingProperties getProperty() {
        return property;
    }

}
