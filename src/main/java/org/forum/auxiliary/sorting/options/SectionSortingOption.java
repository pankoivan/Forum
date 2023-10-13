package org.forum.auxiliary.sorting.options;

import lombok.*;
import org.forum.auxiliary.sorting.enums.SectionSortingProperties;
import org.springframework.data.domain.Sort;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SectionSortingOption {

    private Sort.Direction direction;

    private SectionSortingProperties property;

}
