package org.forum.auxiliary.sorting.options;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.forum.auxiliary.sorting.properties.TopicSortingProperties;
import org.springframework.data.domain.Sort;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TopicSortingOption {

    private Sort.Direction direction;

    private TopicSortingProperties property;

}
